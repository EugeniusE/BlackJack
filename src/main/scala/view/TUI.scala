import scala.io.StdIn.readLine
import util.Observer
import scala.io.StdIn

class TUI(controller: ControllerInterface) extends Observer {

  var log: String = ""
  val inputPromt =
    "Bitte Hit (h), Stand (s), bet(b), Undo (u), Redo (r), oder Quit (q) \n> "

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
            "Bitte Hit (h), Stand (s), bet(b + amount), Undo (u), Redo (r), oder Quit (q) \n> "
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
            case 'b' => {
              val iA = input.split("\\s+")
              if (iA.length == 2) {
                try {
                  val betAmount = iA.array(1).toInt
                  controller.betCommand(betAmount)
                  println(s"you bet ${betAmount}!")
                  printZwischenStand()
                } catch {
                  case e: NumberFormatException =>
                    println(
                      "false Input please enter correctly 'b 100' zB"
                    )
                }
              }
              else 
                println("Please Specify amount to bet")


              print(inputPromt)

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
                "Bitte Hit (h), Stand (s), bet(b) Undo (u), Redo (r), oder Quit (q) \n> "
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
              print(
                "Weiterspielen? (y), Undo (u), Redo (r), oder Quit (q) \n> "
              )
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
        println(controller.getPlayerName() + " hat gewonnen ")
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
          "Bitte Hit (h), Stand (s),bet(b) Undo (u), Redo (r), oder Quit (q) \n> "
        )

      }

      // getInputAndLoop("")

  }

  def printZwischenStand(): Unit = {
    println(
      controller.getPlayerName() + " :" + "Money / Score : " + controller
        .getPlayerMoney()
    )
    println("Einsatz : " + controller.getBet())
    println(HandToString.print_ascii_cards(controller.getPlayerHand()))
    println(
      "Score: " + controller.evaluateHand(
        controller.getPlayerHand()
      )
    )
    println("%50s".format(" ").replace(" ", "="))

    println("Dealer: ")
    println(HandToString.print_ascii_cards(controller.getDealerHand()))
    println(
      "Score: " + controller.evaluateHand(
        controller.getDealerHand()
      )
    )
    println("%50s".format(" ").replace(" ", "="))
  }
}
