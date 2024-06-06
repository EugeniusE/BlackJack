import Decks.Card
import Decks.Deck
import util.Observer
import util.Observable

enum Ergebnis:
  case PlayerWin,DealerWin,Draw,Undecided

class Controller(val game: GameType) extends Observable {

  val table = new Table(game)
  private val commandManager = new CommandManager()

  def newGame(): Unit = {
    table.player.clearHand()
    table.clearDealerhand()
    table.deck = table.deck.shuffle()
    val c1 = drawNewCard()
    val c2 = drawNewCard()
    table.player.addCard(c1)
    table.addDealerHand(c2)
    notifyObservers
  }

  def nextRound(): Unit = {
    table.outcome = Ergebnis.Undecided
    table.player.clearHand()
    table.clearDealerhand()
    table.player.addCard(drawNewCard())
    table.addDealerHand(drawNewCard())
    notifyObservers
  }

  def hit(): Unit = {
    // val command = new HitCommand(table.player, this)
    // command.execute()
    executeCommand(new HitCommand(table.player,this))
    

    println(table.outcome)
    notifyObservers
  }

  def stand(): Unit = {
    // val command = new StandCommand(this)
    // command.execute()
    executeCommand(new StandCommand(this))
    
    println(table.outcome)
    notifyObservers
  }

  def drawNewCard(): Card = {
    if (table.deck.size == 0) {
      table.deck = DeckFactory(FactoryType.StandartDeck).createDeck()
    }

    val (card, remainingDeck) = table.deck.pullFromTop()
    table.deck = remainingDeck
    card
  }
    def executeCommand(command: Command): Unit = {
    commandManager.executeCommand(command)
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


