package se.gigurra.masoncontroller

import com.badlogic.gdx.{ApplicationAdapter, Gdx, Input}
import com.twitter.util.Future
import se.gigurra.fingdx.util.RestClient
import se.gigurra.serviceutils.twitter.logging.Logging

/**
  * Created by kjolh on 4/6/2016.
  */
case class App(playerConfig: PlayerConfig, client: RestClient) extends ApplicationAdapter with Logging {

  import Input.Keys._

  private val keysOfInterest = Set(
    SPACE,
    LEFT,
    RIGHT,
    UP,
    DOWN
  )

  def postKeys(pressed: Set[Int]): Future[Unit] = {
    client.put(s"${playerConfig.instance}/${playerConfig.userName}")(Json.write(PlayerInput(playerConfig.userName, pressed)))
  }

  override def render(): Unit = {
    postKeys(keysOfInterest.filter(Gdx.input.isKeyPressed))
  }

  override def create(): Unit = {
  }
}
