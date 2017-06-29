package clients.kafka

import java.util.Properties
import java.util.concurrent.Future

import common.InitConfs
import common.marketdata.MarketData
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, RecordMetadata}
import org.knowm.xchange.currency.CurrencyPair


class BTCEKafkaProducer extends InitConfs {

  private val topicPrefix: String = "BTCE"

  private val props = new Properties()
  props.put("bootstrap.servers", kafkaHost + ":" + kafkaPort)
  props.put("acks", "all")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "common.serialization.MarketDataSerializer")

  private val kafkaProducer: KafkaProducer[String, MarketData] = new KafkaProducer[String, MarketData](props)

  def send(dataType: String, curencyPair: String, message: MarketData): Future[RecordMetadata] = {
    val record: ProducerRecord[String, MarketData] =
      new ProducerRecord[String, MarketData](topicPrefix + "_" + dataType + "_" + curencyPair.replace("/","-"),
        0,
        System.currentTimeMillis(),
        dataType.toLowerCase,
        message)

    kafkaProducer.send(record)
  }
}
