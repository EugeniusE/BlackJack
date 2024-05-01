import Decks.Card
import Decks.Rank._
import Decks.Suite._
import scala.collection.mutable.ArrayBuffer
import Decks.Deck


enum Ergebnis:
        case PlayerWin, DealerWin, Draw

class Controller {
    

    val table = new Table

    def newGame():Unit = {
        table.deck = table.deck.shuffle()
        val c1 = drawNewCard()
        val c2 = drawNewCard()
        table.player.addCard(c1)
        table.addDealerHand(c2)
    }
    //TODO: mehrer Spielrunden

    // def newRound():Unit = {


    // }


    // Regeln für Spieler Hit
    def hit():Boolean = {

        val card = drawNewCard()

        table.player.addCard(card)

        if(evaluateHand(table.player.getHand()) > 21){
            false
        }   
        else {
            true
       }

    }



    // Dealer actionen es TODO alle regeln implementieren vielleicht mit extra function 
    def stand():Ergebnis = {
        while (evaluateHand(table.getDealerHand()) < 17){
            val card = drawNewCard()
            table.addDealerHand(card)
        }
        val dealerScore = evaluateHand(table.getDealerHand())
        val playerScore = evaluateHand(table.player.getHand())
        if(dealerScore > 21 || dealerScore < playerScore){
            Ergebnis.PlayerWin
        }
        else if(dealerScore == playerScore){
            Ergebnis.Draw
        }
        else
            Ergebnis.DealerWin
        
    }

    def drawNewCard():Card = {
        if(table.deck.size == 0){
        table.deck = new Deck().shuffle()
        }

        val (card,remainingDeck) = table.deck.pullFromTop()
        table.deck = remainingDeck
        card
    }

    // Aufräumen Speichern von State der Klassen des Spiels (Noch nicht implementiert)
    // def quit():Boolean = {
    //     true
    // }



















    // Die Idee ist dass ersmal alle Werte Summiert werden (Asse mit Wert 11)
    // Dann wenn der Wert zu hoch ist, werden solange Asse auf 1 gesetzt bis es entweder keine Asse mehr in der Hand gibt oder 
    // der Wert der Hand unter 21 liegt 

    def evaluateHand(hand:ArrayBuffer[Card]):Int = {
        
        var score = hand.map(_.rank.getRankValue).sum

        if(score > 21 ){
            var aceCount = hand.count(_.rank == Ace)

            while (score > 21 && aceCount > 0){
                score -= 10
                aceCount -= 1 
            }
            }
        

        score
    }
  
}
