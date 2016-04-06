package se.gigurra.masoncontroller

import java.util.UUID

import se.gigurra.serviceutils.twitter.logging.Logging

/**
  * Created by kjolh on 4/6/2016.
  */
object MasonController extends Logging {

  val DEFAULT_HOST = "build.culvertsoft.se"
  val DEFAULT_PORT = "12340"
  val DEFAULT_INSTANCE = "thelostmasons"

  def main(args: Array[String]): Unit = {

    val config = getConfig(args)


    println(config)
  }

  private def getConfig(args: Array[String]): Config = {

    val argMap = args.zipWithIndex.map { case (a, b) => b -> a }.toMap

    val userName = argMap.getOrElse(0, {
      val out = s"Mason-${UUID.randomUUID}"
      logger.warning(s"No name argument, using random name: $out")
      out
    })

    val host = argMap.getOrElse(1, {
      logger.warning(s"No host argument, using default: $DEFAULT_HOST")
      DEFAULT_HOST
    })

    val port = argMap.getOrElse(2, {
      logger.warning(s"No name argument, using default: $DEFAULT_PORT")
      DEFAULT_PORT
    }).toInt

    val instance = argMap.getOrElse(3, {
      logger.warning(s"No instance argument, using default: $DEFAULT_INSTANCE")
      DEFAULT_INSTANCE
    })

    Config(userName, host, port, instance)

  }
}

case class Config(userName: String, host: String, port: Int, instance: String)
case class Input(userName: String, buttonsPressed: Seq[String])
