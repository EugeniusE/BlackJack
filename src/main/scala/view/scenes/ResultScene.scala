import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.{VBox, HBox}
import scalafx.geometry.{Insets, Pos}
import scalafx.event.ActionEvent
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

case class ResultScene(
  windowWidth: Double,
  windowHeight: Double,
  onClickPlayAgainButton: () => Unit = () => println("Play Again")
) extends Scene(windowWidth, windowHeight) {

  private val resultLabel: Label = new Label("")

  val playAgainBtn: Button = HandToString.createNewButton("Play Again!!")
    playAgainBtn.onAction = (_: ActionEvent) => onClickPlayAgainButton()
  
  

  root = new VBox {
    alignment = Pos.Center
    spacing = 10
    padding = Insets(20)
    style = "-fx-background-color: green;"
    children = Seq(
      resultLabel,
      new HBox {
        alignment = Pos.Center
        children = Seq(playAgainBtn)
      }
    )
  }

  def updateResult(winner: String, playerScore: Int, dealerScore: Int): Unit = {
    resultLabel.text = s"Winner: $winner\nPlayer Score: $playerScore\nDealer Score: $dealerScore"
  }
}