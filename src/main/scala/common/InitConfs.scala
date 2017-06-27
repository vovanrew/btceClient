package common

import com.typesafe.config.ConfigFactory

trait InitConfs {

  val confs = ConfigFactory.load()

  val kafkaConf = confs.getConfig("kafkaConfs")

  val kafkaHost = kafkaConf.getString("host")

  val kafkaPort = kafkaConf.getInt("port")


}
