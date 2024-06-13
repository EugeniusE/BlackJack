import Decks.Deck
import Decks.Card
import scala.collection.mutable.ArrayBuffer
import org.hamcrest.Factory
import Ergebnis.PlayerWin
class Table(game: GameType) {
  var deck =
    DeckFactory(FactoryType.StandartDeck)
      .createDeck() // Deck factory zum erstellen von verschiedenen arten von Decks
  private var outcome: Ergebnis = Ergebnis.Undecided
  // var deck = DeckFactory.createDeck()
  private val dealerHand = new ArrayBuffer[Card]

  val player = new Player(game.player.getMoney, game.player.name)

  var playerBet = 0

  def getDealerHand(): ArrayBuffer[Card] = dealerHand

  def addDealerHand(card: Card): Unit = dealerHand.addOne(card)

  def clearDealerhand(): Unit = dealerHand.clear()
  def getOutcome(): Ergebnis = outcome
  def setOutcome(e:Ergebnis):Unit = outcome = e
  def getPlayerHand(): ArrayBuffer[Card] = player.getHand()

}
