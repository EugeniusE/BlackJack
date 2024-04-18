package GameProject

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class CardsValueTest extends AnyWordSpec with Matchers{

  "Deck" should "hold 52 unique cards" in{
    val deck = new Deck()
    deck.cards.size shouldBe 52
  }
    


}
