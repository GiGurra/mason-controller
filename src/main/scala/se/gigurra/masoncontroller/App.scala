package se.gigurra.masoncontroller

import com.badlogic.gdx.{ApplicationAdapter, Gdx, Input}
import com.twitter.util.Future
import se.gigurra.fingdx.util.RestClient
import se.gigurra.serviceutils.twitter.logging.Logging
import se.gigurra.fingdx.gfx.RenderContext._
import se.gigurra.fingdx.gfx.{RenderCenter, World2DProjection}
import se.gigurra.fingdx.lmath.Vec3

/**
  * Created by kjolh on 4/6/2016.
  */
case class App(playerConfig: PlayerConfig, client: RestClient) extends ApplicationAdapter with Logging {

  val keysOfInterest = Set(
    Input.Keys.SPACE,
    Input.Keys.LEFT,
    Input.Keys.RIGHT,
    Input.Keys.UP,
    Input.Keys.DOWN
  )

  implicit val projection = new World2DProjection(new RenderCenter {
    override def position: Vec3 = Vec3(0.0,0.0,0.0)
    override def heading: Double = 0.0
  })

  def postKeys(pressed: Set[Int] = keysOfInterest.filter(Gdx.input.isKeyPressed)): Future[Unit] = {
    client.put(s"${playerConfig.instance}/${playerConfig.userName}")(Json.write(PlayerInput(playerConfig.userName, pressed)))
  }

  override def render(): Unit = {
    postKeys()
    draw()
  }

  private def draw(): Unit = frame {

  }

}
