import scalafx.application.JFXApp3
import scalafx.application.Platform
import scalafx.scene.Scene
import scalafx.stage.{Screen, WindowEvent}
import scalafx.scene.control.{Button, Label}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.{HBox, VBox}
import scalafx.geometry.Pos
import scalafx.Includes._
import javax.print.DocFlavor.INPUT_STREAM
import java.io.FileInputStream
import java.io.InputStream
import scala.compiletime.uninitialized
import scalafx.scene.image.Image
import scalafx.scene.ImageCursor
import control.ControllerInterface

class GUI(controller: ControllerInterface) extends JFXApp3 with util.Observer {
  controller.add(this)

  private var preGameScene: PreGameScene = uninitialized
  private var gameScene: GameScene = uninitialized
  private var resultScene: ResultScene = uninitialized

  private var windowWidth = 1200.0
  private var windowHeight = 740.0

  private val minWindowWidth = 500.0
  private val minWindowHeight = 300.0
  private val cursorImage = new Image(
    new FileInputStream(s"src/main/scala/resources/Chip.png")
  )

  override def start(): Unit = {
    // windowWidth = Screen.primary.bounds.width * 0.85 //if you want screen scaleing
    // windowHeight = Screen.primary.bounds.height * 0.85

    preGameScene = PreGameScene(
      controller,
      windowWidth,
      windowHeight,
      () => {
        stage.setScene(gameScene)
        stage.sizeToScene()
      }
    )
    preGameScene.setCursor(new ImageCursor(cursorImage))
    gameScene = GameScene(
      controller,
      windowWidth,
      windowHeight,
      () => {
        stage.setScene(resultScene)
        stage.sizeToScene()
      }
    )
    gameScene.setCursor(new ImageCursor(cursorImage))
    resultScene = ResultScene(
      windowWidth,
      windowHeight,
      () => {
        stage.setScene(preGameScene)
        stage.sizeToScene()
      }
    )
    resultScene.setCursor(new ImageCursor(cursorImage))

    // val iconImage = new Image(getClass.getResourceAsStream("/Users/simonkann/Documents/Se/BlackJack/src/main/scala/resources/icon.png"))
    stage = new JFXApp3.PrimaryStage {
      height = windowHeight
      width = windowWidth
      scene = preGameScene
      resizable = true
      title = "Blackjack"
      onCloseRequest = (e: WindowEvent) => {
        println("Window closed")
        System.exit(0)
      }

      val primaryScreenBounds = Screen.primary.bounds
      x =
        primaryScreenBounds.getMinX + (primaryScreenBounds.getWidth - windowWidth) / 2
      y =
        primaryScreenBounds.getMinY + (primaryScreenBounds.getHeight - windowHeight) / 2
    }
    controller.newGame()
  }
  def update: Unit = {
    Platform.runLater {
      if (gameScene != null) {
        gameScene.updateGameUI()
      }
    }

  }
}
