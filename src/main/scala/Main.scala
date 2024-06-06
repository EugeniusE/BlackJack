import scala.collection.mutable.ArrayBuffer
import Decks.Card
import Decks.Rank._
import Decks.Suite._
import scala.concurrent.{Await,Future}
import javafx.application.Application

object Main{
  
    // Call the generateField method from the TuiCard object
  val evalStrat =
  new StandardEvaluationStrategy // Different evaluation strategies can be chosen here
  // val controller = new Controller(evalStrat)
  val gameBuilder = new StandardGameBuilder
  gameBuilder.setPlayer("Spieler1", 500)
  val game = gameBuilder.build()
  val controller = new Controller(game)

    val tui = new TUI(controller)
    val gui = new GUI(controller)
    controller.add(gui)
    controller.add(tui)

  def main(args: Array[String]): Unit = {
    new Thread(() => {gui.main(Array.empty)}).start()
    controller.add(tui)
    //controller.add(gui) 

    controller.newGame()
    
    }
  
}