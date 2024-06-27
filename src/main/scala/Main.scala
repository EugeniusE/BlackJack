import scala.collection.mutable.ArrayBuffer
import Decks.Card
import Decks.Rank._
import Decks.Suite._
import scala.io.StdIn.readLine
import scalafx.application.Platform
import scalafx.stage.WindowEvent
import com.google.inject.Guice
import com.google.inject.name.Names
import model._

object Main {

  val evalStrat = new StandardEvaluationStrategy // Different evaluation strategies can be chosen here
  val gameBuilder = new StandardGameBuilder
  gameBuilder.setPlayer("Spieler1", 500)
  gameBuilder.setDeckFactoryType(FactoryType.StandartDeck)
  val game = gameBuilder.build()
  val injector = Guice.createInjector(new BlackJackModule(game))

  // Choose the implementation you want to use (XML or JSON)
  val fileIO = injector.getInstance(classOf[FileIOInterface], Names.named("JSON"))
  val controller = new Controller(game, fileIO)

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
