import Decks.Deck
import Decks.Card
import scala.collection.mutable.ArrayBuffer
import org.hamcrest.Factory
class Table {
  var deck = DeckFactory(FactoryType.StandartDeck).createDeck() // Deck factory zum erstellen von verschiedenen arten von Decks 

  //var deck = DeckFactory.createDeck()
  private val dealerHand = new ArrayBuffer[Card]

  val player = new Player(500,"Spieler1")

  var playerBet = 0


  def getDealerHand():ArrayBuffer[Card]  = dealerHand

  def addDealerHand(card:Card):Unit = dealerHand.addOne(card)

  def clearDealerhand():Unit = dealerHand.clear() 

}
