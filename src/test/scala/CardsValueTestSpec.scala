import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import Decks.Rank._
import Decks.Suite._
import org.scalatest.prop.TableDrivenPropertyChecks._
import Decks.Card

class CardsValueTestSpec extends AnyWordSpec with Matchers{
    "A deck" should {
      val deck = Decks.Deck()
      "have size 52" in{
        deck.size should be(52)

      }
      "contain 52 cards " in {
        deck.getCards.length should be(52)
      }
    }
    "6Decks Deck" should{
      val deck = DeckFactory(FactoryType.SixDecks).createDeck()
      "have size 52 * 6" in {
       deck.size should be(52*6)
      }

    }

    "Shuffle" should{
      val deck = Decks.Deck()
      val deck2 = deck.shuffle()
      "be different from og Deck" in{
        deck should not equal deck2
      }
    }
    "Cards taken from top" should{
      val deck = Decks.Deck()
      val c1 = deck.pullFromTop()
      val c2 = deck.pullFromTop()
      "be 2 different cards" in{
        c1 should not equal c2
      }
    }

    "Cards added to Top" should{
      val deck = Decks.Deck()
      val c1 = new Decks.Card(Ace,Spade)
      val c2 = deck.pullFromTop()
      val deck2 = deck.addToTop(c1)
      "be on top " in{
        val (c3,_) = deck2.pullFromTop()
        c3 shouldEqual c1
      }
    }

    "List of cards added " should{
      val deck = Decks.Deck()
      val c1 = new Decks.Card(Ace,Spade)
      val c2 = new Decks.Card(King,Spade)
      val cL = List(c1,c2)
      val (d2,deck2) = deck.pullFromTop()
      val(d1,deck3) = deck.pullFromTop()
      val deck4 = deck3.addToTop(cL)
      "be added on top" in{
        val(c3,deck5) = deck4.pullFromTop()
        c1 shouldEqual c3
        val(c4,deck6) = deck5.pullFromTop()
        c4 shouldEqual c2 
      }
    }

      "A Controller" should {
        "calculate the total value correctly with multiple cards" in {
          var tuiCard = List[Card](
            Card(Ace, Heart),
            Card(King, Spade),
            Card(Queen, Spade),
            Card(Jack, Spade),
            Card(Ten, Spade),
            Card(Nine, Spade),
            Card(Eight, Spade),
            Card(Seven, Spade),
            Card(Six, Spade),
            Card(Five, Spade),
            Card(Four, Spade),
            Card(Three, Spade),
            Card(Two, Spade)
          )

          val expectedValue = 95

          tuiCard.foreach { card =>
            card.rank.getRankValue should be > 0  // Check that the values are correctly assigned
          }

          val totalValue = tuiCard.map(_.rank.getRankValue).sum
          totalValue shouldEqual expectedValue
      }
    }
}