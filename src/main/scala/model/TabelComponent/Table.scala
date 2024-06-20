import Decks.Deck
import Decks.Card
import scala.collection.mutable.ArrayBuffer
import org.hamcrest.Factory
import Ergebnis._
import com.google.inject.Inject

class Table @Inject(game: GameType) extends TableInterface {

  private var deck =
    DeckFactory.apply(game.deckFactoryType) // Deck factory zum erstellen von verschiedenen arten von Decks
  private var outcome = Ergebnis.Undecided
  // var deck = DeckFactory.createDeck()
  private val dealerHand = new ArrayBuffer[Card]

  private val player = new Player(game.player.getMoney, game.player.name)

  private var playerBet = 0

  def getDealerHand(): ArrayBuffer[Card] = dealerHand

  def addDealerHand(card: Card): Unit = dealerHand.addOne(card)

  def clearDealerhand(): Unit = dealerHand.clear()
  def getOutcome(): Ergebnis = outcome
  def setOutcome(e: Ergebnis): Unit = outcome = e
  def getPlayerHand(): ArrayBuffer[Card] = player.getHand()
  def addPlayerHand(card: Card): Unit = player.addCard(card)
  def removePlayerHand(card: Card): Unit = player.removeCard(card)
  def clearPlayerHand(): Unit = player.clearHand()
  def getDeck(): Deck = deck
  def setDeck(deckP: Deck): Unit = deck = deckP

  def getPlayerName(): String = player.name
  def drawNewCard():Card = {
    val (card, remainingDeck) = deck.pullFromTop()
    deck = remainingDeck
    card
  }

}
