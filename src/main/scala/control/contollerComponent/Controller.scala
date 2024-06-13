import Decks.Card
import Decks.Deck
import util.Observer
import util.Observable
import scala.collection.mutable.ArrayBuffer

enum Ergebnis:
  case PlayerWin, DealerWin, Draw, Undecided

class Controller(val game: GameType) extends ControllerInterface {

  val table = new Table(game)
  private val commandManager = new CommandManager()

  override def newGame(): Unit = {
    table.player.clearHand()
    table.clearDealerhand()
    table.setDeck(table.getDeck().shuffle())
    val c1 = drawNewCard()
    val c2 = drawNewCard()
    table.addPlayerHand(c1)
    table.addDealerHand(c2)
    notifyObservers
  }

  override def nextRound(): Unit = {
    table.setOutcome(Ergebnis.Undecided)
    table.clearPlayerHand()
    table.clearDealerhand()
    table.addPlayerHand(drawNewCard())
    table.addDealerHand(drawNewCard())
    notifyObservers
  }

  override def hit(): Unit = {
    // val command = new HitCommand(table.player, this)
    // command.execute()
    executeCommand(new HitCommand(this))

    println(table.getOutcome())
    notifyObservers
  }

  override def stand(): Unit = {
    // val command = new StandCommand(this)
    // command.execute()
    executeCommand(new StandCommand(this))

    println(table.getOutcome())
    notifyObservers
  }

  override def drawNewCard(): Card = {
    if (table.deck.size == 0) {
      table.deck = DeckFactory.apply(game.deckFactoryType)
    }
    

    val (card, remainingDeck) = table.deck.pullFromTop()
    table.deck = remainingDeck
    card
  }
  override def evaluateHand(hand: ArrayBuffer[Card]): Int =
      game.evalStrat.evaluateHand(hand)
  override def executeCommand(command: Command): Unit = {
    commandManager.executeCommand(command)
  }

  override def undoLastCommand(): Unit = {
    commandManager.undoLastCommand()
    notifyObservers
  }

  override def redoLastUndoneCommand(): Unit = {
    commandManager.redoLastUndoneCommand()
    notifyObservers
  }
  override def getOutcome(): Ergebnis = {
    table.getOutcome()
  }
  override def setOutcome(e: Ergebnis): Unit = table.setOutcome(e)
  override def getDealerHand(): ArrayBuffer[Card] = table.getDealerHand()

  override def addDealerHand(card: Card): Unit = table.addDealerHand(card)

  override def clearDealerhand(): Unit = table.clearDealerhand()
  override def getPlayerHand(): ArrayBuffer[Card] = table.getPlayerHand()
  override def addPlayerHand(card:Card): Unit = table.addPlayerHand(card)
  override def removePlayerHand(card:Card):Unit = table.removePlayerHand(card)
  // override def clearPlayerHand(): Unit = table.clearPlayerHand()
  // override def getDeck(): Deck = table.getDeck()
  override def getPlayerName():String = table.getPlayerName()
}
