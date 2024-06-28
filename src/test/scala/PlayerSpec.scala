import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import util.Decks.{Card,Rank,Suite}
import model.Player


class PlayerSpec extends AnyWordSpec {
  "A Player" when {
    val player = new Player(500, "Player1")
    val card = new Card(Rank.Ace,Suite.Spade)

    "adding a card to its hand" should {
      "correctly add the card" in {
        player.addCard(card)
        player.getHand() should contain(card)
      }
    }

    "clearing its hand" should {
      "result in an empty hand" in {
        player.clearHand()
        player.getHand() shouldBe empty
      }
    }

    "increasing and decreasing money" should {
      "correctly increase the money" in {
        player.increaseMoney(100)
        player.getMoney shouldEqual 600
      }
      
      "correctly decrease the money" in {
        player.decreaseMoney(50)
        player.getMoney shouldEqual 550
      }
    }
  }
}