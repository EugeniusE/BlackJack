package control

import util._
import util.Decks._
import model._
import util.Decks.{Card, Deck}
import util.Observer
import util.Observable
import scala.collection.mutable.ArrayBuffer
import com.google.inject.Inject
import com.google.inject.Guice

class MockController @Inject (game: GameType) extends ControllerInterface() {
  def increasePlayerMoney(amount: Int): Unit = ()
  def decreasePlayerMoney(amount: Int): Unit = ()
  def loadGame(): Unit = ???
  def saveGame(): Unit = ???
  def clearBet(): Unit = ()
  def getBet(): Int = 0
  def setBet(amount: Int): Unit = ???

  def betCommand(amount: Int): Unit = ()
  override def getPlayerMoney(): Int = ???

  private var outcome: Ergebnis = Ergebnis.Undecided
  private var dealerHand: ArrayBuffer[Card] = ArrayBuffer()
  private var playerHand: ArrayBuffer[Card] = ArrayBuffer()

  def newGame(): Unit = {
    println("Mock: Starting a new game")
  }

  def nextRound(): Unit = {
    println("Mock: Starting next round")
  }

  def hit(): Unit = {
    println("Mock: Player hits")
  }

  def stand(): Unit = {
    println("Mock: Player stands")
  }

  def drawNewCard(): Card = {
    println("Mock: Drawing a new card")
    new Card(Rank.Ace, Suite.Club) // Replace with actual mock Card creation
  }

  def evaluateHand(hand: ArrayBuffer[Card]): Int = {
    println(s"Mock: Evaluating hand: $hand")
    21 // Mock evaluation, returning a fixed value
  }

  def executeCommand(command: Command): Unit = {
    println(s"Mock: Executing command: $command")
  }

  def undoLastCommand(): Unit = {
    println("Mock: Undoing last command")
  }

  def redoLastUndoneCommand(): Unit = {
    println("Mock: Redoing last undone command")
  }

  def getOutcome(): Ergebnis = {
    println(s"Mock: Getting outcome: $outcome")
    outcome
  }

  def setOutcome(e: Ergebnis): Unit = {
    println(s"Mock: Setting outcome: $e")
    outcome = e
  }

  def getDealerHand(): ArrayBuffer[Card] = {
    println(s"Mock: Getting dealer hand: $dealerHand")
    dealerHand
  }

  def addDealerHand(card: Card): Unit = {
    println(s"Mock: Adding card to dealer hand: $card")
    dealerHand += card
  }

  def clearDealerhand(): Unit = {
    println("Mock: Clearing dealer hand")
    dealerHand.clear()
  }

  def getPlayerHand(): ArrayBuffer[Card] = {
    println(s"Mock: Getting player hand: $playerHand")
    playerHand
  }

  def addPlayerHand(card: Card): Unit = {
    println(s"Mock: Adding card to player hand: $card")
    playerHand += card
  }

  def removePlayerHand(card: Card): Unit = {
    println(s"Mock: Removing card from player hand: $card")
    playerHand -= card
  }

  def clearPlayerHand(): Unit = {
    println("Mock: Clearing player hand")
    playerHand.clear()
  }

  def getDeck(): Deck = {
    println("Mock: Getting deck")
    new Deck() // Replace with actual mock Deck creation
  }

  def getPlayerName(): String = {
    println("Mock: Getting player name")
    "MockPlayer"
  }
}
