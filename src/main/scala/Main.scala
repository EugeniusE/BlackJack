

import scala.collection.mutable.ArrayBuffer
import control._
import util.Decks.{Rank,Suite,Card}
import util._
import scala.io.StdIn.readLine
import scalafx.application.Platform
import scalafx.stage.WindowEvent
import com.google.inject.Guice
import com.google.inject.name.Names

object Main {

  val evalStrat = new StandardEvaluationStrategy // Different evaluation strategies can be chosen here
  val gameBuilder = new StandardGameBuilder
  gameBuilder.setPlayer("Spieler1", 500) //setUp player TODO accept input
  gameBuilder.setDeckFactoryType(FactoryType.StandartDeck)
  val game = gameBuilder.build()
  val injector = Guice.createInjector(new BlackJackModule(game))

  val controller = new Controller(game)

  val tui = new TUI(controller)
  val gui = new GUI(controller)
  controller.add(gui)
  controller.add(tui)

  def main(args: Array[String]): Unit = {
    var input = ""
    val guiThread = new Thread(() => { gui.main(Array.empty) })
    guiThread.setDaemon(true)
    guiThread.start()

    // Add onCloseRequest handler
    while (input != "q") {
      input = readLine()
      if (input != "q")
        tui.getInputAndLoop(input)
    }
    Platform.exit()
    System.exit(0)
  }
}
