import Decks.Deck
import Decks.Card
import scala.collection.mutable.ArrayBuffer
import org.hamcrest.Factory
import Ergebnis.PlayerWin

class Table(game: GameType) extends TableInterface {

  var deck =
    DeckFactory(FactoryType.StandartDeck)
      .createDeck() // Deck factory zum erstellen von verschiedenen arten von Decks
  var outcome = Ergebnis.Undecided
  // var deck = DeckFactory.createDeck()
  val dealerHand = new ArrayBuffer[Card]

  val player = new Player(game.player.getMoney, game.player.name)

  var playerBet = 0

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

}
