import Decks.Card
import Decks.Deck
import util.Observer
import util.Observable
import scala.collection.mutable.ArrayBuffer
import Main.game
import com.google.inject.Inject
import com.google.inject.Guice
import default.tableI

enum Ergebnis:
  case PlayerWin, DealerWin, Draw, Undecided

class Controller@Inject(game:GameType) extends ControllerInterface() {

  private val table = Guice.createInjector(new BlackJackModule(game)).getInstance(classOf[TableInterface]) 
  private val commandManager = new CommandManager()

  override def newGame(): Unit = {
    table.clearPlayerHand()
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
    table.clearBet()
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
  def betCommand(amount: Int): Unit = executeCommand(new BetCommand(this,amount))
  def setBet(amount: Int): Unit = table.setBet(amount)
  def clearBet(): Unit = table.clearBet()

  override def drawNewCard(): Card = {
    if (table.getDeck().size == 0) {
      table.setDeck(DeckFactory.apply(game.deckFactoryType))
    }
    

    table.drawNewCard()
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
  override def getPlayerName():String = table.getPlayerName()

  override def getDeck():Deck = table.getDeck()

  override def getPlayerMoney(): Int = table.getPlayerMoney()
  override def decreasePlayerMoney(amount: Int): Unit = table.decreasePlayerMoney(amount)
  override def increasePlayerMoney(amount:Int):Unit = table.increasePlayerMoney(amount)
  def getBet(): Int = table.getBet()
}
