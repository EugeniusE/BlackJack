import Decks.Card
import Decks.Deck
import util.Observer
import util.Observable

object Ergebnis extends Enumeration {
  type Ergebnis = Value
  val PlayerWin, DealerWin, Draw = Value
}
import Ergebnis._

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
    val command = new HitCommand(table.player, this)
    command.execute()
    notifyObservers

    if (evaluate.evaluateHand(table.player.getHand()) > 21) {
      false
    } else {
      true
    }
  }

  def stand(): Ergebnis = {
    val command = new StandCommand(this)
    command.execute()
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
