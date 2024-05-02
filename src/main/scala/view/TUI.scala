import scala.io.StdIn.readLine
import util.Observer

 class TUI extends Observer {

  var log:String = ""
  def update: Unit = log.concat("Updated Observer")

  val controller = new Controller

  controller.add(this)
  def start(): Unit = {
    controller.newGame()
    printZwischenStand()
    getInputAndLoop()
  }

  def getInputAndLoop(): Unit = {
    println("Bitte Hit (h) or stand (s) beenden (q)")
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
                    if(continue){
                      getInputAndLoop()
                    }
                    else 
                      //Geld abziehen
                      println("Spieler Verloren")
                
                  }
      case 's' => {
                    println("Dealer ist am Zug .......TODO Implementierung")
                    val ergebis = controller.stand()
                    printZwischenStand()
                    ergebis match
                      case Ergebnis.PlayerWin => {
                        
                        println("Spieler hat gewonnen Gratulation")
                        //Todo Wetteinsatz auszahlen

                      }
                      case Ergebnis.DealerWin => {
                        println("Dealer hat gewonnen")
                        //Wetteinsatz abziehen


                      }
                      case Ergebnis.Draw => {
                        println("Unentschieden")
                        //Jeder Behält sein geld

                      }
                    
                  
                  }
      case _ => { 
                  println("falsche eingabe") 
                  getInputAndLoop()
                  
                }
      // Implement dealer's turn here
    }
  }

  def printZwischenStand():Unit = {
    println(controller.table.player.name+" :")
    println(HandToString.print_ascii_cards(controller.table.player.getHand()))
    println("Score: " + controller.evaluateHand(controller.table.player.getHand()))
    println("%50s".format(" ").replace(" ","="))

    println("Dealer: ")
    println(HandToString.print_ascii_cards(controller.table.getDealerHand()))
    println("Score: " + controller.evaluateHand(controller.table.getDealerHand()))
    println("%50s".format(" ").replace(" ","="))

  }
}
