

import scala.collection.mutable.ArrayBuffer
import Decks.Card
import Decks.Deck

trait  TableInterface {
    def outcome: Ergebnis
    def dealerHand: ArrayBuffer[Card]
    def player:Player
    def deck:Deck
    def getDealerHand():ArrayBuffer[Card]
    def addDealerHand(card: Card): Unit 
    def clearDealerhand(): Unit 
    def getOutcome(): Ergebnis 
    def setOutcome(e:Ergebnis):Unit 
    def getPlayerHand(): ArrayBuffer[Card] 
    def clearPlayerHand():Unit
    def getDeck():Deck
    def setDeck(x:Deck):Unit
    def getPlayerName():String
}
