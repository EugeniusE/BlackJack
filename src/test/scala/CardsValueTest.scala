import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import Decks.Deck

class CardsValueTest extends AnyWordSpec with Matchers{
    "A deck" should {
      val deck = Decks.Deck()
      "have size 52" in{
        deck.size should be(52)

      }

    }
  }
    


