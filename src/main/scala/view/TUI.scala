import scala.io.StdIn.readLine
import util.Observer
import scala.io.StdIn

class TUI(controller: Controller) extends Observer {

  var log: String = ""

  // checkt den aktuellen status nach jeder änderung und gibt dementsprechend auch input output

  def start(): Unit = {
    controller.newGame()
  }

  def getInputAndLoop(input: String): Unit = {
    controller.getOutcome() match
      case Ergebnis.Undecided => {

        if (input.isEmpty) {
          // Prompt for input again
          println("Keine Eingabe!")
          print(
            "Bitte Hit (h), Stand (s), Undo (u), Redo (r), oder Quit (q) \n> "
          )
        } else {
          val char = input.toLowerCase.charAt(
            0
          ) // Convert input to lowercase and get the first character

          char match {
            case 'q' => System.exit(0)
            case 'h' => {
              controller.hit()
            }
            case 's' => {
              controller.stand()
            }
            case 'u' => {
              controller.undoLastCommand()
            }
            case 'r' => {
              controller.redoLastUndoneCommand()

            }
            case _ => {
              println("Falsche Eingabe. Bitte versuchen Sie es erneut.")
              print(
                "Bitte Hit (h), Stand (s), Undo (u), Redo (r), oder Quit (q) \n> "
              )
              // Prompt for input again
            }
          }
        }

      }
      case _ => {

        if (input.isEmpty()) {
          println("Keine Eingabe!")
          print("Weiterspielen? (y), Undo (u), Redo (r), oder Quit (q) \n> ")
          // Prompt for input again
        } else {
          val char = input.toLowerCase.charAt(
            0
          ) // Convert input to lowercase and get the first character

          char match {
            case 'y' => {
              controller.nextRound()
            }
            case 'u' => {
              controller.undoLastCommand()
            }
            case 'r' => {
              controller.redoLastUndoneCommand()
            }
            case 'q' => controller.remove(this)
            case _ => {
              println("Falsche Eingabe")
              print("Weiterspielen? (y), Undo (u), Redo (r), oder Quit (q) \n> ")
              // Prompt for input again
            }
          }
        }

      }

  }
  def update: Unit = {
    printZwischenStand()
    controller.getOutcome() match
      case Ergebnis.PlayerWin => {
        println(controller.table.player.name + " hat gewonnen ")
        print("Weiterspielen? (y), Undo (u), Redo (r), oder Quit (q) \n> ")

      }
      case Ergebnis.DealerWin => {
        println("Dealer hat gewonnen")
        print("Weiterspielen? (y), Undo (u), Redo (r), oder Quit (q) \n> ")

      }
      case Ergebnis.Draw => {
        println("Unentschieden")
        print("Weiterspielen? (y), Undo (u), Redo (r), oder Quit (q) \n> ")

      }
      case Ergebnis.Undecided => {
        print(
          "Bitte Hit (h), Stand (s), Undo (u), Redo (r), oder Quit (q) \n> "
        )

      }

      // getInputAndLoop("")

  }

  def printZwischenStand(): Unit = {
    println(controller.table.player.name + " :")
    println(HandToString.print_ascii_cards(controller.table.player.getHand()))
    println(
      "Score: " + controller.game.evalStrat.evaluateHand(
        controller.table.player.getHand()
      )
    )
    println("%50s".format(" ").replace(" ", "="))

    println("Dealer: ")
    println(HandToString.print_ascii_cards(controller.table.getDealerHand()))
    println(
      "Score: " + controller.game.evalStrat.evaluateHand(
        controller.table.getDealerHand()
      )
    )
    println("%50s".format(" ").replace(" ", "="))
  }
}
