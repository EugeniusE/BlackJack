import scalafx.Includes._
import scalafx.application.JFXApp3
import scalafx.geometry.Pos
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.text.Font
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.application.Platform

class GUI(controller: Controller) extends JFXApp3 with util.Observer {

  var count: Int = 0

  override def start(): Unit = {
    stage = new PrimaryStage {
      title = "Blackjack"
      resizable = false
      scene = new Scene {
        root = new VBox {
          prefWidth = 800
          prefHeight = 500
          alignment = Pos.Center
          spacing = 10
          children = Seq(
            new Label("Welcome to Blackjack!"),
            new Button("Start Game"),
            new Button("Hit"){ onAction = () => controller.hit()}

          )
        }
      }
    }
  }

  def update: Unit = {
    if (stage != null && stage.scene() != null) {
      stage.scene().root() = new VBox {
        prefWidth = 800
        prefHeight = 500
        alignment = Pos.Center
        spacing = 10
        children = Seq(
          new Label(s"Welcome to Blackjack! ($count)"), // Update label text
          new Button("Start Game"),
          new Button("Hit"){ onAction = () => controller.hit()}
        )
      }

      count += 1
    }
  }
}
