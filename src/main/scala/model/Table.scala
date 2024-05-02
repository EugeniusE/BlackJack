import Decks.Deck
import Decks.Card
import scala.collection.mutable.ArrayBuffer
class Table {
  var deck = Decks.Deck()

  private val dealerHand = new ArrayBuffer[Card]

  val player = new Player(500,"Spieler1")

  var playerBet = 0


  def getDealerHand():ArrayBuffer[Card]  = dealerHand

  def addDealerHand(card:Card):Unit = dealerHand.addOne(card)

  def clearDealerhand():Unit = dealerHand.clear() 

}
