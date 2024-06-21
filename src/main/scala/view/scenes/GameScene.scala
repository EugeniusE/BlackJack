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
import javafx.stage.Stage
import scalafx.geometry.Side.Top
import javax.swing.GroupLayout.Alignment
import scalafx.scene.layout.BorderPane
import scalafx.scene.layout.GridPane
import scalafx.scene.layout.Priority
import scalafx.stage.Modality
import scalafx.scene.control.TextField
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType

case class GameScene(
    controller: ControllerInterface,
    windowWidth: Double,
    windowHeight: Double,
    onClickQuitBtn: () => Unit = () => println("Quit")
) extends Scene(windowWidth, windowHeight) {
  val buttonImage = new Image(
    new FileInputStream("src/main/scala/resources/ButtonBackground.png")
  )
  val playerMoneyLabel = new Label() { style = labelStyle }
  val playerBetLabel = new Label() { style = labelStyle }
  val labelStyle =
    "-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: yellow;"
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
  private var remainingCardImages = new StackPane() { alignment = Pos.TOP_LEFT }

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
      root = new BorderPane() {
        hgrow = Priority.ALWAYS
        prefHeight = windowHeight
        prefWidth = windowWidth
        style = "-fx-background-color: green;"
        padding = Insets(10)

        val rCBox = new VBox {
          spacing = 10
          alignment = Pos.TopLeft
          children = Seq(
            new Label("Remaining Cards") {
              style = labelStyle
              padding = Insets(10)
            },
            remainingCardImages // Add remainingCardImages here
          )
        }
        val mBox = new VBox {
          alignment = Pos.TopCenter
          spacing = 10
          children = Seq(
            playerScoreLabel,
            playerCardImages,
            dealerCardImages,
            dealerScoreLabel,
            new Label(message) {
              style =
                "-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: grey;"
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

        val RBox = new VBox { // box zum einfügen rechts Wetten und so
          alignment = Pos.TopRight
          playerBetLabel.setText(s"PlayerBet : ${controller.getBet()}")
          children += playerBetLabel
          style = labelStyle

          playerMoneyLabel.setText(
            s"PlayerMoney: ${controller.getPlayerMoney()}"
          )
          children += playerMoneyLabel

        }
        padding = scalafx.geometry.Insets(10)
        left = rCBox
        right = RBox
        center = mBox

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
      s"Player Score: ${controller.evaluateHand(controller.getPlayerHand())}"
    dealerScoreLabel.text =
      s"Dealer Score: ${controller.evaluateHand(controller.getDealerHand())}"
  }

  def update(): Unit = {
    updateGameUI()
    updateCardImages()
    updateRemainingCardImages()
  }
  // creation of buttons with utility method for same style
  val hit = createNewButton("Hit")
  hit.onAction = _ => {
    controller.hit()
    updateGameUI()
  }
  val stand = createNewButton("Stand")
  stand.onAction = _ => {
    controller.stand()
    updateGameUI()
  }
  val undo = createNewButton("Undo")
  undo.onAction = _ => {
    controller.undoLastCommand()
    updateGameUI()
  }
  val redo = createNewButton("Redo")
  redo.onAction = _ => {
    controller.undoLastCommand()
    updateGameUI()
  }
  val nextRound = createNewButton("Next Round")
  nextRound.onAction = _ => {
    controller.nextRound()
    updateGameUI()
  }
  val quitBtn = createNewButton("quit")
  quitBtn.onAction = _ => { onClickQuitBtn() }
  val betBtn = createNewButton("Place Bet")
  betBtn.onAction = _ => { showBetPopup() }

  continueButtons = Seq(
    hit,
    stand,
    betBtn,
    undo,
    redo
  )
  nextRoundButtons = Seq(
    nextRound,
    undo,
    redo
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
  def createNewButton(buttonText: String): Button = {
    val b = new Button {
      graphic = new StackPane {
        children = new ImageView(buttonImage) {
          fitWidth = 140
          fitHeight = 60
          preserveRatio = true
          smooth = true
        }
        children.add(new Label(buttonText) {
          style = "-fx-text-fill: white; -fx-font-weight: bold;"
        })
      }
      contentDisplay = scalafx.scene.control.ContentDisplay.GraphicOnly
      padding = Insets(0)

    }
    b
  }
  // popup window for placing bets
  def showBetPopup(): Unit = {
    val dialog = new Stage()
    dialog.initModality(Modality.ApplicationModal)
    dialog.setTitle("Place Your Bet")

    val betInput = new TextField { promptText = "Enter Amount to bet" }
    val submitButton = new Button("Submit") {
      onAction = _ => {
        val input = betInput.text.value
        try {
          val betAmount = input.toInt
          controller.betCommand(betAmount)
          playerBetLabel.setText(s"PlayerBet : ${controller.getBet()}")
          playerMoneyLabel.setText(s"PlayerMoney : ${controller.getPlayerMoney()}")

          dialog.close()
        } catch {
          case _: NumberFormatException =>
            new Alert(AlertType.Error) {
              initOwner(dialog)
              title = "Invalid Input"
              headerText = "Invalid Bet Amount"
              contentText = "Please enter a valid number."
            }.showAndWait()
        }
      }
    }

    val vbox = new VBox {
      spacing = 10
      alignment = Pos.Center
      children = Seq(
        new Label("Enter the amount you want to bet:"),
        betInput,
        submitButton
      )
    }

    dialog.setScene(new Scene(vbox, 300, 200))
    dialog.showAndWait()
  }
}
