import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import util.Decks.{Card, Rank, Suite}
import model.Player

class PlayerSpec extends AnyWordSpec {
  "A Player" when {
    val player = new Player(500, "Player1")
    val card = new Card(Rank.Ace, Suite.Spade)

    "adding a card to its hand" should {
      "correctly add the card" in {
        player.addCard(card)
        player.getHand() should contain(card)
      }
    }

    "removing a card from its hand" should {
      "correctly remove the card" in {
        val card2 = new Card(Rank.King, Suite.Heart)
        player.addCard(card2)
        player.removeCard(card2)
        player.getHand() should not contain(card2)
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

    "setting player's money" should {
      "correctly set the player's money" in {
        player.setPlayerMoney(1000)
        player.getMoney shouldEqual 1000
      }
    }
  }
}
