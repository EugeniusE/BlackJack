import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import scala.collection.mutable.ArrayBuffer
import util.Decks.{Card, Rank, Suite, Deck}
import util._
import control._
import model.Player
import java.nio.file.{Files, Paths}
import java.nio.file.Path
class FileIoSpec extends AnyWordSpec {

  val gameBuilder = new StandardGameBuilder
  gameBuilder.setPlayer("Spieler1", 500) // setUp player TODO accept input
  gameBuilder.setDeckFactoryType(FactoryType.StandartDeck)
  val game = gameBuilder.build()

  "FileIo " when {
    "saving and loading game" should {
      "save and load the game state correctly using JSONFileIO" in {
        val controller = new Controller(game)
        controller.newGame()
        val card = controller.getPlayerHand().apply(0)
        Files.delete(Paths.get("game.json"))
        controller.saveGame()

        val newController = new Controller(game)
        newController.loadGame()

        newController.getPlayerHand() should contain(card)

        // "save and load the game state correctly using XMLFileIO" in {
        //   val gameWithXML = new GameType(new StandardEvaluationStrategy, FactoryType.StandartDeck, new Player(500, "Spieler1"))
        //   val xmlFileIO = new XMLFileIO(gameWithXML)
        //   val controller = new Controller(gameWithXML) {
        //   }
        //   controller.newGame()
        //   controller.addPlayerHand(new Card(Rank.Ten, Suite.Spade))
        //   controller.saveGame()

        //   val newController = new Controller(gameWithXML) {
        //   }
        //   newController.loadGame()

        //   newController.getPlayerHand() should contain (new Card(Rank.Ten, Suite.Spade))
        // }
      }
    }
  }

}
