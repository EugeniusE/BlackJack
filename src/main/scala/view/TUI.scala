import scala.io.StdIn.readLine
import util.Observer

class TUI extends Observer {

  var log: String = ""
  def update: Unit = log.concat("Updated Observer")
  val evalStrat = new StandardEvaluationStrategy // Es können verschiedene Evaluations-Regeln hier ausgewählt werden
  val controller = new Controller(evalStrat)

  controller.add(this)
  def start(): Unit = {
    controller.newGame()
    printZwischenStand()
    getInputAndLoop()
  }

  

  def getInputAndLoop(): Unit = {
    print("Bitte Hit (h) or stand (s) beenden (q) \n> ")
    val chars = readLine().toCharArray()

    if (chars.length != 1) {
      println("falsche eingabe")
      getInputAndLoop()
    }
    val eingabe = chars(0).toLower match {
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
          case Ergebnis.DealerWin => {println("Dealer hat gewonnen")}
          case Ergebnis.Draw => println("Unentschieden")
        }
        nextRound()
      }
      case _ => {
        println("falsche eingabe")
        getInputAndLoop()
      }
    }
  }




  def nextRound(): Unit = {
    print("Weiterspielen? y/n \n> ")
    val chars = readLine().toCharArray()
    if(chars.length != 1){
      println("Falsche eingabe")
      nextRound()
    }
    chars(0).toLower match {
      case 'y' => {  
                    controller.nextRound()
                    printZwischenStand()
                    getInputAndLoop()
                  }
      case 'n' => controller.remove(this)
      case _  => {
                  println("Falsche Eingabe")
                  nextRound()
                  } 
    }
  }

  def printZwischenStand(): Unit = {
    println(controller.table.player.name + " :")
    println(HandToString.print_ascii_cards(controller.table.player.getHand()))
    println("Score: " + controller.evaluate.evaluateHand(controller.table.player.getHand()))
    println("%50s".format(" ").replace(" ", "="))

    println("Dealer: ")
    println(HandToString.print_ascii_cards(controller.table.getDealerHand()))
    println("Score: " + controller.evaluate.evaluateHand(controller.table.getDealerHand()))
    println("%50s".format(" ").replace(" ", "="))
  }
}
