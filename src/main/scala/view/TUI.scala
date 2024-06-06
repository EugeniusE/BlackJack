import scala.io.StdIn.readLine
import util.Observer
import scala.io.StdIn

class TUI extends Observer {

  var log: String = ""

  //checkt den aktuellen status nach jeder änderung und gibt dementsprechend auch input output


  val evalStrat =
    new StandardEvaluationStrategy // Different evaluation strategies can be chosen here
  // val controller = new Controller(evalStrat)
  val gameBuilder = new StandardGameBuilder
  gameBuilder.setPlayer("Spieler1", 500)
  val game = gameBuilder.build()
  val controller = new Controller(game)

  controller.add(this)

  def start(): Unit = {
    controller.newGame()
  }

  def getInputAndLoop(): Unit = {
    print("Bitte Hit (h), Stand (s), Undo (u), Redo (r), oder Quit (q) \n> ")
    val input = readLine()

    if (input.isEmpty) {
      println("Keine Eingabe. Bitte versuchen Sie es erneut.")
      getInputAndLoop() // Prompt for input again
    } else {
      val char = input.toLowerCase.charAt(
        0
      ) // Convert input to lowercase and get the first character

      char match {
        case 'q' => controller.remove(this)
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
          getInputAndLoop() // Prompt for input again
        }
      }
    }
  }
    def update: Unit = {
    printZwischenStand()
    controller.table.outcome match
      case Ergebnis.PlayerWin => {
        println(controller.table.player.name + " hat gewonnen ")
        nextRound()
      }
      case Ergebnis.DealerWin => {
        println("Dealer hat gewonnen")
        nextRound()

      }
      case Ergebnis.Draw => {
        println("Unentschieden")
        nextRound()
      }
      case Ergebnis.Undecided =>{
        getInputAndLoop()
      }

  }

  def nextRound(): Unit = {
    print("Weiterspielen? (y), Undo (u), Redo (r), oder Quit (q) \n> ")
    val input = readLine()

    if (input.isEmpty()) {
      println("Keine Eingabe. Bitte versuchen Sie es erneut.")
      nextRound() // Prompt for input again
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
          nextRound() // Prompt for input again
        }
      }
    }
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
