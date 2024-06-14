import scalafx.application.JFXApp3
import scalafx.application.Platform
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.{HBox, VBox}
import scalafx.geometry.Pos
import scalafx.Includes._
import javax.print.DocFlavor.INPUT_STREAM
import java.io.FileInputStream
import java.io.InputStream
import javafx.stage.WindowEvent
import scala.compiletime.uninitialized
import scalafx.scene.image.Image

class GUI(controller: Controller) extends JFXApp3 with util.Observer {
  controller.add(this)

  //private var preGameScene: PreGameScene = _
  //private var gameScene: GameScene = _
  //private var resultScene: ResultScene = _

  private var preGameScene: PreGameScene = uninitialized
  private var gameScene: GameScene = uninitialized
  private var resultScene: ResultScene = uninitialized

  private val windowWidth = 1440
  private val windowHeight = 960

  private val minWindowWidth = 500
  private val minWindowHeight = 300

  override def start(): Unit = {
    preGameScene = PreGameScene(controller, windowWidth, windowHeight, () => stage.setScene(gameScene))
    gameScene = GameScene(controller, windowWidth, windowHeight, () => stage.setScene(resultScene))
    resultScene = ResultScene(windowWidth, windowHeight, () => stage.setScene(preGameScene))

    //val iconImage = new Image(getClass.getResourceAsStream("/Users/simonkann/Documents/Se/BlackJack/src/main/scala/resources/icon.png"))
    stage = new JFXApp3.PrimaryStage {
      resizable = false
      title = "Blackjack"
      scene = preGameScene
      minWidth = minWindowWidth
      minHeight = minWindowHeight
      //icons += iconImage

      // override close button function
      onCloseRequest = (e: WindowEvent) => {
        println("Window closed")
        System.exit(0)
      }
    }
  }

  def update: Unit = {
    Platform.runLater{
      if(gameScene != null){
        gameScene.updateGameUI()
      }
    }
  }
}