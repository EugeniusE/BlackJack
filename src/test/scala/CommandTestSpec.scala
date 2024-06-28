import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import java.io.{ByteArrayInputStream, ByteArrayOutputStream, PrintStream}
import org.hamcrest.Matcher
import scala.util.Try
import scala.util.Failure
import scala.util.Success
import util.Decks.Deck
import control._
import util._
import model._

class CommandTestSpec extends AnyWordSpec with Matchers {
  val game = new GameType(new StandardEvaluationStrategy,FactoryType.StandartDeck,new Player(500,"Spieler1"))
   "A CommandManager" when {
    "undoing the last action" should {
      "restore the previous state" in {
        // Create a mock Controller
        val controller = new Controller(game)

        // Perform some actions (for example, hitting)
        controller.hit()
        val initialPlayerHandSize = controller.getPlayerHand().size
        controller.hit()
        // Undo the action
        controller.undoLastCommand()

        // Assert that the player's hand size is restored to the initial size
        controller.getPlayerHand().size should be (initialPlayerHandSize)
      }
     }

      "redoing the previously undone action" should {
      "reapply the undone action" in {
        // Create a mock Controller
        val controller = new Controller(game)

        // Perform some actions (for example, hitting)
        controller.hit()

        // Store the player's hand size after hitting
        val handSizeAfterHit = controller.getPlayerHand().size

        // Undo the action
        controller.undoLastCommand()

        // Redo the previously undone action
        controller.redoLastUndoneCommand()

        // Assert that the player's hand size is restored to the size after hitting
        controller.getPlayerHand().size should be(handSizeAfterHit)
      }
    }

    "undoing when doing nothing" should {
        "do nothing when hit and stand" in{
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
          val gameBuilder = new StandardGameBuilder
          gameBuilder.setEvaluationStrategy(new StandardEvaluationStrategy)
          gameBuilder.setDeckFactoryType(FactoryType.StandartDeck)
            val controller = new Controller(game)   
            controller.newGame()
            val initialDealerHand = controller.getDealerHand()
            controller.stand() // Execute stand command
            controller.undoLastCommand() // Undo the stand command

            // Verify that the dealer's hand is restored to its initial state
            controller.getDealerHand().toList shouldEqual initialDealerHand
        }
    }
  }

    "CommandManager" should {"handle command execution failure" in {
            // Create a mock command that always fails during execution
            val failingCommand = new Command {
            override def execute(): Try[Unit] = Failure(new RuntimeException("Command execution failed"))
            override def undo(): Try[Unit] = ???
            }

            // Create a CommandManager instance
            val commandManager = new CommandManager

            // Execute the failing command
            commandManager.executeCommand(failingCommand)

            // Verify that the command manager prints the error message
            // Add your assertions here based on the expected behavior
        }
             "not redo if undone command execution fails" in {
                val commandManager = new CommandManager
                val undoneCommand = DummyCommand()
                
                // Push the undone command to undoneCommands stack
                commandManager.undoneCommands.push(undoneCommand)
                
                // Attempt to redo the undone command
                commandManager.redoLastUndoneCommand()
                
                // Ensure that executedCommands stack remains empty
                assert(commandManager.executedCommands.isEmpty)
                
                // Ensure that the undoneCommands stack still contains the failed command
                assert(commandManager.undoneCommands.size == 1)
                assert(commandManager.undoneCommands.top == undoneCommand)
        }
              "handle undo command failure" in {
            // Create a mock command that always fails during undo
            val failingUndoCommand = new Command {
            override def execute(): Try[Unit] = Success(())
            override def undo(): Try[Unit] = Failure(new RuntimeException("Undo command failed"))
            }

            // Create a CommandManager instance
            val commandManager = new CommandManager

            // Push the failing undo command onto the executed commands stack
            commandManager.executedCommands.push(failingUndoCommand)

            // Attempt to undo the last command
            commandManager.undoLastCommand()

            // Verify that the CommandManager handles the failure during undo
            // Add your assertions here based on the expected behavior
        }
    }
     // Define a dummy Command implementation for testing
  case class DummyCommand() extends Command {
    override def execute(): Try[Unit] = Failure(new RuntimeException("Execution failed"))
    override def undo(): Try[Unit] = Success(())
  }  
}