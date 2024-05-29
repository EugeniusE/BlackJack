import scala.io.StdIn.readLine
import util.Observer
import scala.io.StdIn

class TUI extends Observer {

  var log: String = ""
  def update: Unit = log.concat("Updated Observer")
  val evalStrat = new StandardEvaluationStrategy // Different evaluation strategies can be chosen here
  val controller = new Controller(evalStrat)

  controller.add(this)

  def start(): Unit = {
    controller.newGame()
    printZwischenStand()
    getInputAndLoop()
  }

  def getInputAndLoop(): Unit = {
    print("Bitte Hit (h), Stand (s), Undo (u), Redo (r), oder Quit (q) \n> ")
    val input = readLine()

    if (input.isEmpty) {
      println("Keine Eingabe. Bitte versuchen Sie es erneut.")
      getInputAndLoop() // Prompt for input again
    } else {
      val char = input.toLowerCase.charAt(0) // Convert input to lowercase and get the first character

      char match {
        case 'q' => controller.remove(this)
        case 'h' => {
          val continue = controller.hit()
          printZwischenStand()
          if (continue) {
            getInputAndLoop()
          } else {
            println("Spieler Verloren")
            nextRound()
          }
        }
        case 's' => {
          println("Dealer ist am Zug .......")
          val ergebnis = controller.stand()
          printZwischenStand()
          ergebnis match {
            case Ergebnis.PlayerWin => println("Spieler hat gewonnen Gratulation")
            case Ergebnis.DealerWin => println("Dealer hat gewonnen")
            case Ergebnis.Draw => println("Unentschieden")
          }
          nextRound()
        }
        case 'u' => {
          controller.undoLastCommand()
          printZwischenStand()
          getInputAndLoop()
        }
        case 'r' => {
          controller.redoLastUndoneCommand()
          printZwischenStand()
          getInputAndLoop()
        }
        case _ => {
          println("Falsche Eingabe. Bitte versuchen Sie es erneut.")
          getInputAndLoop() // Prompt for input again
        }
      }
    }
  }

  def nextRound(): Unit = {
    print("Weiterspielen? (y), Undo (u), Redo (r), oder Quit (q) \n> ")
    val input = readLine()
    
    if (input.isEmpty()) {
      println("Keine Eingabe. Bitte versuchen Sie es erneut.")
      nextRound() // Prompt for input again
    } else {
      val char = input.toLowerCase.charAt(0) // Convert input to lowercase and get the first character

      char match {
        case 'y' => {
          controller.nextRound()
          printZwischenStand()
          getInputAndLoop()
        }
        case 'u' => {
          controller.undoLastCommand()
          printZwischenStand()
          getInputAndLoop() // Prompt for input again
        }
        case 'r' => {
          controller.redoLastUndoneCommand()
          printZwischenStand()
          getInputAndLoop() // Prompt for input again
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
      "Score: " + controller.evaluate.evaluateHand(
        controller.table.player.getHand()
      )
    )
    println("%50s".format(" ").replace(" ", "="))

    println("Dealer: ")
    println(HandToString.print_ascii_cards(controller.table.getDealerHand()))
    println(
      "Score: " + controller.evaluate.evaluateHand(
        controller.table.getDealerHand()
      )
    )
    println("%50s".format(" ").replace(" ", "="))
  }
}
