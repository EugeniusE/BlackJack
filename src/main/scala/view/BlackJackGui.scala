import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.VBox
import scalafx.geometry.Pos
import scalafx.Includes._

class GUI(controller: Controller) extends JFXApp3 with util.Observer {

  private var continueButtons: Seq[Button] = _
  private var nextRoundButtons: Seq[Button] = _

  override def start(): Unit = {
    val (continueBtns, nextRoundBtns) = makeButtons()
    continueButtons = continueBtns
    nextRoundButtons = nextRoundBtns

    stage = new JFXApp3.PrimaryStage {
      title = "Blackjack"
      scene = new Scene {
        root = new VBox {
          prefWidth = 800
          prefHeight = 500
          alignment = Pos.Center
          spacing = 10
          children = Seq(
            new Label("Welcome to Blackjack!"),
            new Button("Start Game") {
              onAction = _ => {
                controller.newGame()
              }
            }
          )
        }
      }
    }
  }

  override def update: Unit = {
    updateGameUI()
  }

  private def updateGameUI(): Unit = {
    val (message, buttons) = controller.table.outcome match {
      case Ergebnis.Undecided =>
        ("Game is ongoing, make your move.", continueButtons)
      case Ergebnis.DealerWin =>
        ("Dealer wins!", nextRoundButtons)
      case Ergebnis.PlayerWin =>
        (s"${controller.table.player.name} wins!", nextRoundButtons)
      case Ergebnis.Draw =>
        ("It's a draw!", nextRoundButtons)
    }

    if (stage != null && stage.scene() != null) {
      stage.scene().root = new VBox {
        prefWidth = 800
        prefHeight = 500
        alignment = Pos.Center
        spacing = 10
        children = Seq(
          new Label("Welcome to Blackjack!"),
          new Label(message),
          new Button("Start Game") {
            onAction = _ => {
              controller.newGame()
            }
          }
        ) ++ buttons
      }
    }
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
}
