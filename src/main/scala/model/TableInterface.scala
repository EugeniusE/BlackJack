

import scala.collection.mutable.ArrayBuffer
import Decks.Card
import Decks.Deck

trait  TableInterface {
    def getDealerHand():ArrayBuffer[Card]
    def addDealerHand(card: Card): Unit 
    def clearDealerhand(): Unit 
    def getOutcome(): Ergebnis 
    def setOutcome(e:Ergebnis):Unit 
    def getPlayerHand(): ArrayBuffer[Card] 
    def addPlayerHand(card: Card):Unit
    def clearPlayerHand():Unit
    def removePlayerHand(card:Card):Unit
    def getDeck():Deck
    def setDeck(x:Deck):Unit
    def getPlayerName():String
    def drawNewCard():Card
}
