import Decks.Card
import Decks.Rank._
import Decks.Suite._
import scala.collection.mutable.ArrayBuffer
import Decks.Deck
import util.Observer
import util.Observable

enum Ergebnis:
  case PlayerWin, DealerWin, Draw

class Controller(val evaluate: EvaluationStrategy) extends Observable {

  val table = new Table

  def newGame(): Unit = {
    table.deck = table.deck.shuffle()
    val c1 = drawNewCard()
    val c2 = drawNewCard()
    table.player.addCard(c1)
    table.addDealerHand(c2)
    notifyObservers
  }

  def hit(): Boolean = {
    val card = drawNewCard()
    table.player.addCard(card)
    notifyObservers

    if (evaluate.evaluateHand(table.player.getHand()) > 21) {
      false
    } else {
      true
    }
  }

  def stand(): Ergebnis = {
    while (evaluate.evaluateHand(table.getDealerHand()) < 17) {
      val card = drawNewCard()
      table.addDealerHand(card)
    }
    val dealerScore = evaluate.evaluateHand(table.getDealerHand())
    val playerScore = evaluate.evaluateHand(table.player.getHand())
    notifyObservers
    if (dealerScore > 21 || dealerScore < playerScore) {
      Ergebnis.PlayerWin
    } else if (dealerScore == playerScore) {
      Ergebnis.Draw
    } else {
      Ergebnis.DealerWin
    }
  }

  def drawNewCard(): Card = {
    if (table.deck.size == 0) {
      table.deck = new Deck().shuffle()
    }

    val (card, remainingDeck) = table.deck.pullFromTop()
    table.deck = remainingDeck
    notifyObservers
    card
  }
}
