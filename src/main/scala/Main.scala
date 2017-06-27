import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import clients.xchange.BTCEClient
import common.BTCECurrencies

import scala.concurrent.duration._

object Main extends App {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val dispatcher = system.dispatcher

  val btce = system.actorOf(BTCEClient.props)

  def queryData = {
    btce ! BTCEClient.GetTrages(BTCECurrencies.BTC_USD)
    btce ! BTCEClient.GetTrages(BTCECurrencies.BTC_ETH)
    btce ! BTCEClient.GetTrages(BTCECurrencies.BTC_XMR)


    btce ! BTCEClient.GetTicker(BTCECurrencies.BTC_USD)
    btce ! BTCEClient.GetTicker(BTCECurrencies.BTC_ETH)
    btce ! BTCEClient.GetTicker(BTCECurrencies.BTC_XMR)
  }

  system.scheduler.schedule(0 second, 20 second, btce,BTCEClient.GetOrderBook(BTCECurrencies.BTC_USD, 5))
  system.scheduler.schedule(0 second, 20 second, btce,BTCEClient.GetOrderBook(BTCECurrencies.BTC_ETH, 5))
  system.scheduler.schedule(0 second, 20 second, btce,BTCEClient.GetOrderBook(BTCECurrencies.BTC_XMR, 5))

  system.scheduler.schedule(0 second, 2 seconds)(queryData)
}
