import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import scala.collection.mutable.ArrayBuffer
import util.Decks.{Card, Rank, Suite, Deck}
import util._
import control._
import model.Player

class ControllerSpec extends AnyWordSpec {
  val game = new GameType(
    new StandardEvaluationStrategy,
    FactoryType.StandartDeck,
    new Player(500, "Spieler1")
  )

  "A Controller" when {

    "a new game is started" should {
      "initialize a new game with non-empty deck and hands" in {
        val controller = new Controller(game)
        val tui = new TUI(controller)
        controller.add(tui)
        controller.remove(tui)
        controller.newGame()

        controller.getDeck().size should be > 0
        controller.getPlayerHand() should not be empty
        controller.getDealerHand() should not be empty
        controller.subscribers.length shouldEqual 0
      }
    }

    "a new round is started each player" should {
      "initialize a new round" in {
        val controller = new Controller(game)
        controller.newGame()
        controller.nextRound()
        controller.getDeck().size should be > 0
        controller.getPlayerHand() should not be empty
        controller.getDealerHand() should not be empty
      }
    }

    "hitting" should {
      "add a card to the player's hand" in {
        val controller = new Controller(game)
        controller.newGame()
        val initialHandSize = controller.getPlayerHand().size

        controller.hit()

        controller.getPlayerHand().size shouldEqual initialHandSize + 1
      }
      "return dealer win" in {
        val controller = new Controller(game)
        controller.newGame()
        controller.addPlayerHand(Card(Rank.Ten, Suite.Spade))
        controller.addPlayerHand(Card(Rank.Queen, Suite.Spade))
        controller.addPlayerHand(Card(Rank.Jack, Suite.Spade))
        controller.hit()
        controller.getOutcome() shouldEqual Ergebnis.DealerWin
      }
    }

    "standing" should {
      "return the correct Ergebnis PlayerWin" in {
        val controller = new Controller(game)
        controller.addPlayerHand(new Card(Rank.Ace, Suite.Spade))
        controller.addPlayerHand(new Card(Rank.King, Suite.Heart))
        controller.addDealerHand(new Card(Rank.Ten, Suite.Club))
        controller.addDealerHand(new Card(Rank.Ten, Suite.Club))

        controller.stand()
        controller.getOutcome() shouldEqual Ergebnis.PlayerWin
      }
      "return the correct Ergebnis DealerWin" in {
        val controller = new Controller(game)
        controller.addPlayerHand(new Card(Rank.Ten, Suite.Spade))
        controller.addDealerHand(new Card(Rank.Nine, Suite.Heart))
        controller.addDealerHand(new Card(Rank.Ten, Suite.Club))
        controller.stand()
        controller.getOutcome() shouldEqual Ergebnis.DealerWin
      }
      "return the correct Ergebnis Draw" in {
        val controller = new Controller(game)
        controller.addPlayerHand(new Card(Rank.Ten, Suite.Spade))
        controller.addPlayerHand(new Card(Rank.Eight, Suite.Spade))
        controller.addDealerHand(new Card(Rank.Ten, Suite.Heart))
        controller.addDealerHand(new Card(Rank.Eight, Suite.Club))
        controller.stand()
        controller.getOutcome() shouldEqual Ergebnis.Draw
      }
    }

    "evaluating hand" should {
      "correctly evaluate the hand value" in {
        val controller = new Controller(game)
        val hand = ArrayBuffer(
          new Card(Rank.Ace, Suite.Spade),
          new Card(Rank.Seven, Suite.Heart)
        )

        controller.evaluateHand(hand) shouldEqual 18
      }
    }

    "Dealer hand" should {
      "be empty" in {
        val controller = new Controller(game)
        controller.clearDealerhand()
        controller.getDealerHand().size shouldEqual 0
      }
      "get Cards" in {
        val controller = new Controller(game)
        controller.addPlayerHand((new Card(Rank.Ace, Suite.Spade)))
        controller.stand()
        controller.getDealerHand().size should be > 1
      }
    }

    "A new Deck is made" should {
      "appear when drawing 52 cards" in {
        val c = new Controller(game)
        for (_ <- 0 to 52) {
          c.drawNewCard()
        }
        c.getDeck().size shouldEqual 51
      }
    }

    "A high stakes Game" should {
      val eval = new HighStakes
      val hand = ArrayBuffer(
        new Card(Rank.Ace, Suite.Spade),
        new Card(Rank.Ace, Suite.Spade)
      )
      "evaluate 2 aces as 22" in {
        eval.evaluateHand(hand) shouldEqual 22
      }
    }

    "betting" should {
      "set and clear bet correctly" in {
        val controller = new Controller(game)
        controller.newGame()
        controller.betCommand(100)
        controller.getBet() shouldEqual 100
        controller.clearBet()
        controller.getBet() shouldEqual 0
      }
    }

    "saving and loading game" should {
      "save and load the game state correctly using JSONFileIO" in {
        val controller = new Controller(game)
        controller.newGame()
        val card = controller.getPlayerHand().apply(0)
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

      "undoing and redoing commands" should {
        "undo the last command correctly" in {
          val controller = new Controller(game)
          controller.newGame()
          val initialHandSize = controller.getPlayerHand().size

          controller.hit()
          controller.getPlayerHand().size shouldEqual initialHandSize + 1

          controller.undoLastCommand()
          controller.getPlayerHand().size shouldEqual initialHandSize
        }
        "redo the last undone command correctly" in {
          val controller = new Controller(game)
          controller.newGame()
          val initialHandSize = controller.getPlayerHand().size

          controller.hit()
          controller.getPlayerHand().size shouldEqual initialHandSize + 1

          controller.undoLastCommand()
          controller.getPlayerHand().size shouldEqual initialHandSize

          controller.redoLastUndoneCommand()
          controller.getPlayerHand().size shouldEqual initialHandSize + 1
        }
      }
    }
  }
}
