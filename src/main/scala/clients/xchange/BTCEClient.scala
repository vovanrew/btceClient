package clients.xchange

import java.util.Properties

import akka.actor.{Actor, ActorLogging, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import clients.kafka.BTCEKafkaProducer
import common.InitConfs
import common.marketdata.{OrderBook, Ticker, Trade}
import org.knowm.xchange.btce.v3.BTCEExchange
import org.knowm.xchange.currency.CurrencyPair
import org.knowm.xchange.dto.marketdata
import org.knowm.xchange.dto.trade.LimitOrder
import org.knowm.xchange.service.marketdata.MarketDataService
import org.knowm.xchange.{Exchange, ExchangeFactory}

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}


object BTCEClient {

  case object GetConnectionState

  case class GetOrderBook(pair: CurrencyPair, depth: Int)
  case class GetTicker(pair: CurrencyPair)
  case class GetTrages(pair: CurrencyPair)

  def props: Props = Props(new BTCEClient)
}


class BTCEClient extends Actor with ActorLogging with InitConfs {

  import BTCEClient._
  import context.dispatcher

  val exchange: Exchange = ExchangeFactory
  .INSTANCE
  .createExchange((new BTCEExchange).getClass.getName)

  val marketDataService: MarketDataService = exchange.getMarketDataService

  var connectionStatus: Boolean = false

  final implicit val materializer: ActorMaterializer =
  ActorMaterializer(ActorMaterializerSettings(context.system))

  val props = new Properties()
  props.put("bootstrap.servers", kafkaHost + ":" + kafkaPort.toString)
  props.put("acks", "all")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "common.serialization.MarketDataSerializer")

  private var lastTradeId: Long = 1

  private var lastOrderBookId: Long = 1

  private val kafkaProducer: BTCEKafkaProducer = new BTCEKafkaProducer(props)

  override def receive: Receive = {

    case GetConnectionState =>

      Http(context.system).singleRequest(HttpRequest(uri = "https://btc-e.com/api/3/ticker/btc_usd-btc_rur")).onComplete {
        case Success(responce) =>
          connectionStatus = true
          log.info("Connection to BTCE is success")
          sender() ! connectionStatus

        case Failure(exception) =>
          connectionStatus = false
          log.error("Connection to BTCE is failed")
          sender() ! connectionStatus
      }


    case GetOrderBook(pair, depth) =>
      val orderBook = marketDataService.getOrderBook(CurrencyPair.BTC_USD, depth.asInstanceOf[Object])

      kafkaProducer.send("ORDERBOOK", pair, OrderBook(orderBook.getTimeStamp,
        orderBook.getAsks.asScala.toList, orderBook.getBids.asScala.toList))


    case GetTicker(pair) =>
      val ticker = marketDataService.getTicker(pair)

      kafkaProducer.send("TICKER", ticker.getCurrencyPair,
        Ticker(ticker.getCurrencyPair,
          ticker.getLast,
          ticker.getBid,
          ticker.getAsk,
          ticker.getHigh,
          ticker.getLow,
          ticker.getVwap,
          ticker.getVolume,
          ticker.getTimestamp))


    case GetTrages(pair) =>
      val tryTrades =
        Try(marketDataService.getTrades(pair, 50.asInstanceOf[Object]))

      tryTrades match {
        case Success(trades) => {
          trades.getTrades.asScala
            .filter { trade: marketdata.Trade => trade.getId.toLong > lastTradeId }
            .foreach { trade =>
              kafkaProducer.send("TRADE", pair,
                Trade(trade.getType,
                  trade.getTradableAmount,
                  trade.getCurrencyPair,
                  trade.getPrice,
                  trade.getTimestamp,
                  trade.getId))
            }

          lastTradeId = trades.getTrades.get(trades.getTrades.size() - 1).getId.toLong
        }

        case Failure(e) =>
          println(e + "\n\n\n")
      }
  }
}
