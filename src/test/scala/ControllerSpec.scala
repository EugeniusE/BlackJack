import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import scala.collection.mutable.ArrayBuffer
import Decks.Card
import Decks.Rank
import Decks.Suite

class ControllerSpec extends AnyWordSpec {
  "A Controller" when {
    "a new game is started" should {
      "initialize a new game with non-empty deck and hands" in {
        val controller = new Controller
        controller.newGame()

        controller.table.deck.size should be > 0
        controller.table.player.getHand() should not be empty
        controller.table.getDealerHand() should not be empty
      }
    }

    "hitting" should {
      "add a card to the player's hand" in {
        val controller = new Controller
        controller.newGame()
        val initialHandSize = controller.table.player.getHand().size

        controller.hit()

        controller.table.player.getHand().size shouldEqual initialHandSize + 1
      }
      "return false if players hand is over 21" in {
        val controller = new Controller
        controller.newGame()
        controller.table.player.addCard(Card(Rank.Ten,Suite.Spade))
        controller.table.player.addCard(Card(Rank.Queen,Suite.Spade))
        controller.table.player.addCard(Card(Rank.Jack,Suite.Spade))

        controller.hit() shouldEqual false

      }
    }

    "standing" should {
      "return the correct Ergebnis PlayerWin" in {
        val controller = new Controller
        controller.table.player.addCard(new Card(Rank.Ace, Suite.Spade))
        controller.table.player.addCard(new Card(Rank.Nine, Suite.Heart))
        controller.table.addDealerHand(new Card(Rank.Ten, Suite.Club))

        controller.stand() shouldEqual Ergebnis.PlayerWin
      }
      "return the correct Ergebnis DealerWin" in {
        val controller = new Controller
        controller.table.player.addCard(new Card(Rank.Ace, Suite.Spade))
        controller.table.addDealerHand(new Card(Rank.Nine, Suite.Heart))
        controller.table.addDealerHand(new Card(Rank.Ten, Suite.Club))
        controller.stand() shouldEqual Ergebnis.DealerWin
      }
      "return the correct Ergebnis Draw" in {
        val controller = new Controller
        controller.table.player.addCard(new Card(Rank.Ace, Suite.Spade))
        controller.table.player.addCard(new Card(Rank.Eight, Suite.Spade))
        controller.table.addDealerHand(new Card(Rank.Ten, Suite.Heart))
        controller.table.addDealerHand(new Card(Rank.Nine, Suite.Club))
        controller.stand() shouldEqual Ergebnis.Draw
      }
    }

    "evaluating hand" should {
      "correctly evaluate the hand value" in {
        val controller = new Controller
        val hand = ArrayBuffer(new Card(Rank.Ace, Suite.Spade), new Card(Rank.Seven, Suite.Heart))

        controller.evaluateHand(hand) shouldEqual 18
      }
    }

    "Dealer hand " should{
        "Be empty " in {
            val controller = new Controller
            controller.table.clearDealerhand()
            controller.table.getDealerHand().size shouldEqual 0
        }
    }

    "A new Deck is made" should {
    "apper when drawing 52 cards" in {
        val c = new Controller()
            for(_<-0 to 52){
                c.drawNewCard()
            }
        c.table.deck.size shouldEqual 51

      }
    }



  }
}
