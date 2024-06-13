import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.layout.{HBox, VBox}
import scalafx.geometry.{Insets, Pos}
import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.scene.control.{Button, Label}
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
import javafx.css.Style
import javafx.stage.WindowEvent

case class PreGameScene(
  controller: Controller,
  windowWidth: Double,
  windowHeight: Double,
  onClickStartGameButton: () => Unit = () => println("Start Game")
) extends Scene(windowWidth, windowHeight) {

  val startGameBtn: Button = new Button("Start Game") {
    onAction = (_: ActionEvent) => {
      controller.newGame()
      onClickStartGameButton()
    }
  }

  onKeyPressed = (event) => {
    if (event.code.toString == "ENTER") {
      startGameBtn.fire()
    }
  }

  root = new VBox {
    alignment = Pos.Center
    spacing = 10
    padding = Insets(20)
    children = Seq(
      new HBox {
        alignment = Pos.Center
        children = Seq(
          new Label("Welcome to Blackjack!")
        )
      },
      new HBox {
        alignment = Pos.Center
        children = Seq(
          startGameBtn
        )
      }
    )
  }
}