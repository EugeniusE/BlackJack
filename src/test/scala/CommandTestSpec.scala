import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import scala.util.{Try, Failure, Success}
import util.Decks.{Card, Rank, Suite}
import control._
import util._
import model._

class CommandTestSpec extends AnyWordSpec with Matchers {
  val game = new GameType(new StandardEvaluationStrategy, FactoryType.StandartDeck, new Player(500, "Spieler1"))

  "A CommandManager" when {
    "undoing the last action" should {
      "restore the previous state" in {
        val controller = new Controller(game)
        controller.hit()
        val initialPlayerHandSize = controller.getPlayerHand().size
        controller.hit()
        controller.undoLastCommand()
        controller.getPlayerHand().size should be(initialPlayerHandSize)
      }
    }

    "redoing the previously undone action" should {
      "reapply the undone action" in {
        val controller = new Controller(game)
        controller.hit()
        val handSizeAfterHit = controller.getPlayerHand().size
        controller.undoLastCommand()
        controller.redoLastUndoneCommand()
        controller.getPlayerHand().size should be(handSizeAfterHit)
      }
    }

    "undoing when doing nothing" should {
      "do nothing when hit and stand" in {
        val controller = new Controller(game)
        val iph = controller.getPlayerHand()
        val idh = controller.getDealerHand()
        val hitCommand = new HitCommand(controller)
        val standCommand = new StandCommand(controller)
        hitCommand.undo()
        standCommand.undo()
        iph shouldEqual controller.getPlayerHand()
        idh shouldEqual controller.getDealerHand()
      }
    }

    "undoing stand" should {
      "undo stand" in {
        val controller = new Controller(game)
        controller.newGame()
        val initialDealerHand = controller.getDealerHand()
        controller.stand() 
        controller.undoLastCommand() 
        controller.getDealerHand().toList shouldEqual initialDealerHand
      }
    }

    "handling command execution failure" should {
      "handle command execution failure" in {
        val failingCommand = new Command {
          override def execute(): Try[Unit] = Failure(new RuntimeException("Command execution failed"))
          override def undo(): Try[Unit] = ???
        }
        val commandManager = new CommandManager
        commandManager.executeCommand(failingCommand)
        // Verify expected behavior, such as ensuring no state change or logging error
      }

      "not redo if undone command execution fails" in {
        val commandManager = new CommandManager
        val undoneCommand = DummyCommand()
        commandManager.undoneCommands.push(undoneCommand)
        commandManager.redoLastUndoneCommand()
        assert(commandManager.executedCommands.isEmpty)
        assert(commandManager.undoneCommands.size == 1)
        assert(commandManager.undoneCommands.top == undoneCommand)
      }

      "handle undo command failure" in {
        val failingUndoCommand = new Command {
          override def execute(): Try[Unit] = Success(())
          override def undo(): Try[Unit] = Failure(new RuntimeException("Undo command failed"))
        }
        val commandManager = new CommandManager
        commandManager.executedCommands.push(failingUndoCommand)
        commandManager.undoLastCommand()
        // Verify expected behavior, such as ensuring state is not corrupted or logging error
      }
    }
  }

  "A HitCommand" should {
    "execute correctly and add a card to player's hand" in {
      val controller = new Controller(game)
      controller.newGame()
      val command = new HitCommand(controller)
      val initialHandSize = controller.getPlayerHand().size
      command.execute()
      controller.getPlayerHand().size shouldEqual initialHandSize + 1
    }

    "undo correctly and remove the last card from player's hand" in {
      val controller = new Controller(game)
      controller.newGame()
      val command = new HitCommand(controller)
      command.execute()
      val initialHandSize = controller.getPlayerHand().size
      command.undo()
      controller.getPlayerHand().size shouldEqual initialHandSize - 1
    }
  }

  "A StandCommand" should {
    "execute correctly and update the game outcome" in {
      val controller = new Controller(game)
      controller.newGame()
      controller.addPlayerHand(new Card(Rank.Ten, Suite.Spade))
      controller.addPlayerHand(new Card(Rank.Ten, Suite.Heart))
      controller.addDealerHand(new Card(Rank.Nine, Suite.Club))
      val command = new StandCommand(controller)
      command.execute()
      controller.getOutcome() shouldEqual Ergebnis.PlayerWin
    }

    "undo correctly and restore the dealer's hand and game outcome" in {
      val controller = new Controller(game)
      controller.newGame()
      val initialDealerHand = controller.getDealerHand()
      val command = new StandCommand(controller)
      command.execute()
      command.undo()
      controller.getDealerHand().toList shouldEqual initialDealerHand.toList
      controller.getOutcome() shouldEqual Ergebnis.Undecided
    }
  }

  "A BetCommand" should {
    "execute correctly and update the player's money and bet amount" in {
      val controller = new Controller(game)
      controller.newGame()
      val initialMoney = controller.getPlayerMoney()
      val betAmount = 50
      val command = new BetCommand(controller, betAmount)
      command.execute()
      controller.getPlayerMoney() shouldEqual initialMoney - betAmount
      controller.getBet() shouldEqual betAmount
    }

    "undo correctly and restore the player's money and clear the bet amount" in {
      val controller = new Controller(game)
      controller.newGame()
      val initialMoney = controller.getPlayerMoney()
      val betAmount = 50
      val command = new BetCommand(controller, betAmount)
      command.execute()
      command.undo()
      controller.getPlayerMoney() shouldEqual initialMoney
      controller.getBet() shouldEqual 0
    }
  }

  case class DummyCommand() extends Command {
    override def execute(): Try[Unit] = Failure(new RuntimeException("Execution failed"))
    override def undo(): Try[Unit] = Success(())
  }
}
