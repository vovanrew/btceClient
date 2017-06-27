package common.serialization

import java.util

import common.marketdata.MarketData
import org.apache.kafka.common.serialization.Serializer
import MarshallableImplicits._


class MarketDataSerializer extends Serializer[MarketData]{

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {}

  override def serialize(topic: String, data: MarketData): Array[Byte] = data.toJson.getBytes

  override def close(): Unit = {}
}
