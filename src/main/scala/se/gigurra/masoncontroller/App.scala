package se.gigurra.masoncontroller

import com.badlogic.gdx.{ApplicationAdapter, Gdx, Input}
import com.twitter.util.Future
import se.gigurra.fingdx.util.RestClient
import se.gigurra.serviceutils.twitter.logging.Logging
import se.gigurra.fingdx.gfx.RenderContext._
import se.gigurra.fingdx.gfx.{GfxConfig, RenderCenter, World2DProjection}
import se.gigurra.fingdx.lmath.{Vec2, Vec3}

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

  implicit val drawCfg = new GfxConfig {
    override def symbolScale: Double = 1.0
  }

  def postKeys(pressed: Set[Int] = keysOfInterest.filter(isPressed)): Future[Unit] = {
    client.put(s"${playerConfig.instance}/${playerConfig.userName}")(Json.write(PlayerInput(playerConfig.userName, pressed)))
  }

  override def render(): Unit = frame {
    postKeys()
    drawTitle()
    drawArrowKeys()
    drawSpaceBar()
  }

  def drawTitle() = {
    at((0.0, yTitle)) {
      s"MASON CONTROLLER".drawCentered(WHITE, scale = 3.0f)
    }
    at((0.0, yTitle - 0.2)) {
      s"Player: ${playerConfig.userName}".drawCentered(WHITE, scale = 1.5f)
    }
  }

  def drawArrowKeys() = {
    at(arrowKeyCenter - (keySpacing, 0.0))(drawArrowKey(Input.Keys.LEFT))
    at(arrowKeyCenter + (keySpacing, 0.0))(drawArrowKey(Input.Keys.RIGHT))
    at(arrowKeyCenter + (0.0, keySpacing))(drawArrowKey(Input.Keys.UP))
    at(arrowKeyCenter - (0.0, keySpacing))(drawArrowKey(Input.Keys.DOWN))
  }

  def drawSpaceBar() = {
    at(spacebarCenter) {
      rect(keyWidth * 4, keyHeight, typ = if (isPressed(Input.Keys.SPACE)) FILL else LINE, color = WHITE)
    }
  }

  def drawArrowKey(i: Int): Unit = {
    rect(keyWidth, keyHeight, typ = if (isPressed(i)) FILL else LINE, color = WHITE)
  }

  def isPressed(i: Int): Boolean = {
    Gdx.input.isKeyPressed(i)
  }

  val yTitle = 0.625
  val yKeys = -0.1
  val keyWidth = 0.1
  val keyHeight = 0.1
  val keySpacing = math.max(keyWidth, keyHeight) * 1.5
  val arrowKeyCenter = Vec2(0.3, yKeys)
  val spacebarCenter = Vec2(-0.3, yKeys)

}
