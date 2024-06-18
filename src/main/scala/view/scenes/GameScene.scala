import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.{HBox, VBox, StackPane}
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.image.{Image, ImageView}
import java.io.FileInputStream
import scalafx.application.Platform
import scalafx.scene.Scene
import scalafx.event.ActionEvent
import scala.compiletime.uninitialized
import javafx.stage.WindowEvent
import scalafx.geometry.Side.Top
import javax.swing.GroupLayout.Alignment

case class GameScene(
    controller: Controller,
    windowWidth: Double,
    windowHeight: Double,
    onClickQuitBtn: () => Unit = () => println("Quit")
) extends Scene(windowWidth, windowHeight) {
  val buttonImage = new Image(new FileInputStream("src/main/scala/resources/ButtonBackground.png"))
  private val playerCardImages = new HBox {
    alignment = Pos.Center
    spacing = 10
  }
  private val dealerCardImages = new HBox {
    alignment = Pos.Center
    spacing = 10
  }
  private val playerScoreLabel = new Label("Player Score: 0") {
    style = "-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;"
  }
  private val dealerScoreLabel = new Label("Dealer Score: 0") {
    style = "-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;"
  }

  private var continueButtons: Seq[Button] = uninitialized
  private var nextRoundButtons: Seq[Button] = uninitialized
  private var remainingCardImages = new StackPane(){alignment = Pos.TOP_LEFT}
  val quitBtn: Button = new Button("Quit") {
    onAction = _ => onClickQuitBtn()
  }
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
        (s"${controller.getPlayerName()} wins!", nextRoundButtons)
      case Ergebnis.Draw =>
        ("It's a draw!", nextRoundButtons)
    }

     Platform.runLater {
    root = new StackPane() {
      style = "-fx-background-color: green;"
      alignment = Pos.TOP_LEFT// Adjust spacing between elements as needed
      padding = Insets(10)

      children = Seq(
        new VBox {
          spacing = 10
          alignment = Pos.BASELINE_LEFT
          children = Seq(
            new Label("Remaining Cards") {
              style =
                "-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: yellow;"
              padding = Insets(10)
            },
            remainingCardImages // Add remainingCardImages here
          )
        },
        new VBox {
          alignment = Pos.BASELINE_CENTER
          spacing = 10
          children = Seq(
            playerScoreLabel,
            playerCardImages,
            dealerCardImages,
            dealerScoreLabel,
            new Label(message) {
              style =
                "-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: blue;"
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
        },
        new VBox{ // box zum einfügen rechts Wetten und so
          alignment = Pos.BASELINE_RIGHT
          children += new Label("Top Right")
        }
      )
    }
    updateCardImages()
    updateScores()
    updateRemainingCardImages()
    }
  }

  def updateCardImages(): Unit = {
    playerCardImages.children.clear()
    dealerCardImages.children.clear()

    controller.getPlayerHand().foreach { card =>
      val inputStream = new FileInputStream(
        s"src/main/scala/resources/cards2.0/${cardPath(card)}.png"
      )
      val cardImage = new ImageView(new Image(inputStream)) {
        fitHeight = 200
        fitWidth = 140
      }
      playerCardImages.children.add(cardImage)
    }

    controller.getDealerHand().foreach { card =>
      val inputStream = new FileInputStream(
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
    updateCardImages()
    updateRemainingCardImages()
  }

  continueButtons = Seq(
    new Button("Hit") {
      graphic = new StackPane {
        children = new ImageView(buttonImage) {
          fitWidth = 140
          fitHeight = 60
          preserveRatio = true
          smooth = true
        }
        children.add(new Label("Hit") {
          style = "-fx-text-fill: white; -fx-font-weight: bold;"
        })
      }
      contentDisplay = scalafx.scene.control.ContentDisplay.GraphicOnly
      padding = Insets(0)
      onAction = _ => {
        controller.hit()
        updateGameUI()
      }
    },
    new Button("Stand") {
      graphic = new StackPane {
        children = new ImageView(buttonImage) {
          fitWidth = 140
          fitHeight = 60
          preserveRatio = true
          smooth = true
        }
        children.add(new Label("Stand") {
          style = "-fx-text-fill: white; -fx-font-weight: bold;"
        })
      }
      contentDisplay = scalafx.scene.control.ContentDisplay.GraphicOnly
      padding = Insets(0)
      onAction = _ => {
        controller.stand()
        updateGameUI()
      }
    },
    new Button("Undo") {
      graphic = new StackPane {
        children = new ImageView(buttonImage) {
          fitWidth = 140
          fitHeight = 60
          preserveRatio = true
          smooth = true
        }
        children.add(new Label("Undo") {
          style = "-fx-text-fill: white; -fx-font-weight: bold;"
        })
      }
      contentDisplay = scalafx.scene.control.ContentDisplay.GraphicOnly
      padding = Insets(0)
      onAction = _ => {
        controller.undoLastCommand()
        updateGameUI()
      }
    },
    new Button("Redo") {
      graphic = new StackPane {
        children = new ImageView(buttonImage) {
          fitWidth = 140
          fitHeight = 60
          preserveRatio = true
          smooth = true
        }
        children.add(new Label("Redo") {
          style = "-fx-text-fill: white; -fx-font-weight: bold;"
        })
      }
      contentDisplay = scalafx.scene.control.ContentDisplay.GraphicOnly
      padding = Insets(0)
      onAction = _ => {
        controller.redoLastUndoneCommand()
        updateGameUI()
      }
    }
  )

  private def updateRemainingCardImages(): Unit = {
    remainingCardImages.children.clear()

    // Load the card back image once
    val inputStream = new FileInputStream(
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
          index * 5 // Adjust this value to control the vertical stacking
      }
      remainingCardImages.children.add(cardImage)
    }
  }
}
