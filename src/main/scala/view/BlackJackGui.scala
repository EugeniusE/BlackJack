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
import scalafx.scene.layout.StackPane

class GUI(controller: Controller) extends JFXApp3 with util.Observer {
  val style1: String =
    "-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;"

  private var continueButtons: Seq[Button] = _
  private var nextRoundButtons: Seq[Button] = _
  private val playerCardImages = new HBox()
  private val dealerCardImages = new HBox()
  private var playerScoreLabel: Label = _
  private var dealerScoreLabel: Label = _
  private val remainingCardImages = new StackPane() {
    alignment = Pos.TOP_RIGHT
  }

  override def start(): Unit = {
    playerScoreLabel = new Label("Player Score: 0") { style = style1 }
    dealerScoreLabel = new Label("Dealer Score: 0") { style = style1 }

    val (continueBtns, nextRoundBtns) = makeButtons()
    continueButtons = continueBtns
    nextRoundButtons = nextRoundBtns

    stage = new JFXApp3.PrimaryStage {
      title = "Blackjack"
      scene = new Scene {
        root = new VBox {
          prefWidth = 1500
          prefHeight = 800
          alignment = Pos.Center
          spacing = 10
          children = Seq(
            new Label("Welcome to Blackjack!"),
            playerCardImages,
            // playerScoreLabel,
            dealerCardImages,
            // dealerScoreLabel,
            new Button("Start Game") {
              onAction = _ => {
                controller.newGame()
              }
            }
          )
        }
      }
      onCloseRequest = (e: WindowEvent) => {
        println("Window closed")
        System.exit(0)
      }
    }
  }

  override def update: Unit = {

    updateGameUI()

  }

  private def updateGameUI(): Unit = {

    val (message, buttons) = controller.getOutcome() match {
      case Ergebnis.Undecided =>
        ("Game is ongoing, make your move.", continueButtons)
      case Ergebnis.DealerWin =>
        ("Dealer wins!", nextRoundButtons)
      case Ergebnis.PlayerWin =>
        (s"${controller.getPlayerName()} wins!", nextRoundButtons)
      case Ergebnis.Draw =>
        ("It's a draw!", nextRoundButtons)
    }

    if (stage != null && stage.scene() != null) {
      Platform.runLater {
        () => // wichtig fals der andere thread aufruft sonst error wegen thread verletzung
          stage.scene().root = new VBox {
            style = "-fx-background-color: green;"
            prefWidth = 1630
            prefHeight = 900
            alignment = Pos.Center
            spacing = 10
            children = Seq(
              new HBox {
                alignment = Pos.TOP_CENTER
                spacing = 500
                children =
                  Seq(new Label("Black Jack !!!!"), remainingCardImages)
              },
              new Label(s"Remaining Cards : ${controller.getDeck().size}"),
              playerCardImages,
              playerScoreLabel,
              dealerCardImages,
              dealerScoreLabel,
              new Label(message) {
                style =
                  "-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: red;"
              },
              new HBox {
                alignment = Pos.Center
                spacing = 10
                children = buttons
              }
            ) // ++ buttons
          }

          updateCardImages()
          updateRemainingCardImages()
          updateScores()
      }
    }

  }

  private def updateCardImages(): Unit = {
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
  private def updateScores(): Unit = {
    playerScoreLabel.text =
      s"Player Score: ${controller.evaluateHand(controller.getPlayerHand())}"
    dealerScoreLabel.text =
      s"Dealer Score: ${controller.evaluateHand(controller.getDealerHand())}"
  }

  def makeButtons(): (Seq[Button], Seq[Button]) = {
    val nextRoundButtons: Seq[Button] = Seq(
      new Button("Next Round") {
        onAction = _ => {
          controller.nextRound()
        }
      },
      new Button("Undo") {
        onAction = _ => {
          controller.undoLastCommand()
        }
      },
      new Button("Redo") {
        onAction = _ => {
          controller.redoLastUndoneCommand()
        }
      }
    )

    val continueButtons: Seq[Button] = Seq(
      new Button("Hit") {
        onAction = _ => {
          controller.hit()
        }
      },
      new Button("Stand") {
        onAction = _ => {
          controller.stand()
        }
      },
      new Button("Undo") {
        onAction = _ => {
          controller.undoLastCommand()
        }
      },
      new Button("Redo") {
        onAction = _ => {
          controller.redoLastUndoneCommand()
        }
      }
    )

    (continueButtons, nextRoundButtons)
  }
  private def updateRemainingCardImages(): Unit = {
    remainingCardImages.children.clear()

    // Load the card back image once
    val inputStream: InputStream = new FileInputStream(
      "src/main/scala/resources/cards2.0/cardBack.png"
    )
    val cardBackImage = new Image(inputStream)

    // Create a single ImageView instance for the card back image
    val cardBackImageView = new ImageView(cardBackImage) {
      fitHeight = 200
      fitWidth = 140
    }

    // Add multiple instances of the ImageView to the StackPane
    controller.getDeck().getCards.zipWithIndex.foreach { case (_, index) =>
      val cardImage = new ImageView(cardBackImage) {
        fitHeight = cardBackImageView.fitHeight()
        fitWidth = cardBackImageView.fitWidth()
        translateY =
          index * 10 // Adjust this value to control the vertical stacking
      }
      remainingCardImages.children.add(cardImage)
    }
  }
}
