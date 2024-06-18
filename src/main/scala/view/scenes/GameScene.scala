import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.{HBox, VBox}
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.image.{Image, ImageView}
import java.io.{FileInputStream, InputStream}
import scalafx.application.Platform
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
import scalafx.event.ActionEvent

case class GameScene(
  controller: Controller,
  windowWidth: Double,
  windowHeight: Double,
  onClickQuitBtn: () => Unit = () => println("Quit")
) extends Scene(windowWidth, windowHeight) {

  private val playerCardImages = new HBox {
    alignment = Pos.Center
    spacing = 10
  }
  private val dealerCardImages = new HBox {
    alignment = Pos.Center
    spacing = 10
  }
  private val playerScoreLabel = new Label("Player Score: 0")
  private val dealerScoreLabel = new Label("Dealer Score: 0")

  private var continueButtons: Seq[Button] = _
  private var nextRoundButtons: Seq[Button] = _

  val quitBtn: Button = new Button("Quit") {
    onAction = (_: ActionEvent) => onClickQuitBtn()
  }

  continueButtons = Seq(
    new Button("Hit") {
      onAction = _ => {
        controller.hit()
        updateGameUI()
      }
    },
    new Button("Stand") {
      onAction = _ => {
        controller.stand()
        updateGameUI()
      }
    },
    new Button("Undo") {
      onAction = _ => {
        controller.undoLastCommand()
        updateGameUI()
      }
    },
    new Button("Redo") {
      onAction = _ => {
        controller.redoLastUndoneCommand()
        updateGameUI()
      }
    }
  )

  nextRoundButtons = Seq(
    new Button("Next Round") {
      onAction = _ => {
        controller.nextRound()
        updateGameUI()
      }
    },
    new Button("Undo") {
      onAction = _ => {
        controller.undoLastCommand()
        updateGameUI()
      }
    },
    new Button("Redo") {
      onAction = _ => {
        controller.redoLastUndoneCommand()
        updateGameUI()
      }
    }
  )

  def updateGameUI(): Unit = {
    val (message, buttons) = controller.getOutcome() match {
      case Ergebnis.Undecided =>
        ("Game is ongoing, make your move.", continueButtons)
      case Ergebnis.DealerWin =>
        ("Dealer wins!", nextRoundButtons)
      case Ergebnis.PlayerWin =>
        (s"${controller.table.player.name} wins!", nextRoundButtons)
      case Ergebnis.Draw =>
        ("It's a draw!", nextRoundButtons)
    }

    Platform.runLater {
      root = new VBox {
        style = "-fx-background-color: green;"
        prefWidth = windowWidth
        prefHeight = windowHeight
        alignment = Pos.Center
        spacing = 10
        children = Seq(
          new Label("Welcome to Blackjack!"),
          playerCardImages,
          playerScoreLabel,
          dealerCardImages,
          dealerScoreLabel,
          new Label(message) {
            style = "-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: red;"
          },
          new HBox {
            alignment = Pos.Center
            spacing = 10
            children = buttons
          },
          new HBox {
            alignment = Pos.Center
            children = Seq(quitBtn)
          }
        )
      }
      updateCardImages()
      updateScores()
    }
  }

  def updateCardImages(): Unit = {
    playerCardImages.children.clear()
    dealerCardImages.children.clear()

    controller.getPlayerHand().foreach { card =>
      val inputStream: InputStream = new FileInputStream(
        s"src/main/scala/resources/cards2.0/${cardPath(card)}.png"
      )
      val cardImage = new ImageView(new Image(inputStream)) {
        fitHeight = 200
        fitWidth = 140
      }
      playerCardImages.children.add(cardImage)
    }

    controller.getDealerHand().foreach { card =>
      val inputStream: InputStream = new FileInputStream(
        s"src/main/scala/resources/cards2.0/${cardPath(card)}.png"
      )
      val cardImage = new ImageView(new Image(inputStream)) {
        fitHeight = 200
        fitWidth = 140
      }
      dealerCardImages.children.add(cardImage)
    }
  }

  def updateScores(): Unit = {
    playerScoreLabel.text =
      s"Player Score: ${controller.game.evalStrat.evaluateHand(controller.getPlayerHand())}"
    dealerScoreLabel.text =
      s"Dealer Score: ${controller.game.evalStrat.evaluateHand(controller.getDealerHand())}"
  }

  def update(): Unit = {
    updateGameUI()
  }
}