package clients.kafka

import java.util.Properties
import java.util.concurrent.Future

import common.marketdata.MarketData
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, RecordMetadata}
import org.knowm.xchange.currency.CurrencyPair


class BTCEKafkaProducer(props: Properties) extends KafkaProducer[String, MarketData](props){

  private val topicPrefix: String = "BTCE"

  def send(dataType: String, curencyPair: CurrencyPair, message: MarketData): Future[RecordMetadata] = {
    val record: ProducerRecord[String, MarketData] =
      new ProducerRecord[String, MarketData](topicPrefix + "_" + dataType + "_" + curencyPair.toString.replace("/","-"),
        0,
        System.currentTimeMillis(),
        curencyPair.toString,
        message)

    super.send(record)
  }
}
