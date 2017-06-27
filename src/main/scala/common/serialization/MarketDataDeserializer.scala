package common.serialization

import java.util

import common.marketdata.MarketData
import common.serialization.MarshallableImplicits._
import org.apache.kafka.common.serialization.Deserializer


class MarketDataDeserializer extends Deserializer[MarketData]{

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {}

  override def close(): Unit = {}

  override def deserialize(topic: String, data: Array[Byte]): MarketData = data.toString.fromJson()
}
