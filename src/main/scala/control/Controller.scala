import Decks.Card
import Decks.Deck
import util.Observer
import util.Observable

object Ergebnis extends Enumeration {
  type Ergebnis = Value
  val PlayerWin, DealerWin, Draw = Value
}
import Ergebnis._

class Controller(val game: GameType) extends Observable {

  val table = new Table(game)
  private val commandManager = new CommandManager()

  def newGame(): Unit = {
    table.deck = table.deck.shuffle()
    val c1 = drawNewCard()
    val c2 = drawNewCard()
    table.player.addCard(c1)
    table.addDealerHand(c2)
    notifyObservers
  }

  def nextRound(): Unit = {
    table.player.clearHand()
    table.clearDealerhand()
    table.player.addCard(drawNewCard())
    table.addDealerHand(drawNewCard())
  }

  def hit(): Boolean = {
    // val command = new HitCommand(table.player, this)
    // command.execute()
    executeCommand(new HitCommand(table.player,this))
    notifyObservers

    if (game.evalStrat.evaluateHand(table.player.getHand()) > 21) {
      false
    } else {
      true
    }
  }

  def stand(): Ergebnis = {
    // val command = new StandCommand(this)
    // command.execute()
    executeCommand(new StandCommand(this))
    
    val dealerScore = game.evalStrat.evaluateHand(table.getDealerHand())
    val playerScore = game.evalStrat.evaluateHand(table.player.getHand())
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
      table.deck = DeckFactory(FactoryType.StandartDeck).createDeck()
    }

    val (card, remainingDeck) = table.deck.pullFromTop()
    table.deck = remainingDeck
    notifyObservers
    card
  }
    def executeCommand(command: Command): Unit = {
    commandManager.executeCommand(command)
    notifyObservers
  }

  def undoLastCommand(): Unit = {
    commandManager.undoLastCommand()
    notifyObservers
  }

  def redoLastUndoneCommand(): Unit = {
    commandManager.redoLastUndoneCommand()
    notifyObservers
  }
}


