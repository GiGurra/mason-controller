package se.gigurra.masoncontroller

import java.util.UUID

import com.badlogic.gdx.backends.lwjgl.{LwjglApplication, LwjglApplicationConfiguration}
import com.twitter.util.Await
import se.gigurra.fingdx.util.RestClient
import se.gigurra.serviceutils.twitter.logging.Logging

/**
  * Created by kjolh on 4/6/2016.
  */
object MasonController extends Logging {

  val DEFAULT_HOST = "build.culvertsoft.se"
  val DEFAULT_PORT = "12340"
  val DEFAULT_INSTANCE = "thelostmasons"

  def main(args: Array[String]): Unit = {

    val playerConfig = getPlayerConfig(args)
    val lwjglConfig = getLwjglConfig(args)

    val client = RestClient(playerConfig.host, playerConfig.port, "game server")
    val app = App(playerConfig, client)

    // Check in.. see if the connection works
    Await.result(app.postKeys(Set.empty))

    // Now start it!
    new LwjglApplication(app, lwjglConfig)
  }

  private def getPlayerConfig(args: Array[String]): PlayerConfig = {

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

    PlayerConfig(userName, host, port, instance)

  }



  def getLwjglConfig(args: Array[String]): LwjglApplicationConfiguration = {
    new LwjglApplicationConfiguration {
      title = "Mason Controller"
      forceExit = false
      vSyncEnabled = true
      foregroundFPS = 60
      backgroundFPS = 60
      samples = 4
      resizable = false
    }
  }

}



