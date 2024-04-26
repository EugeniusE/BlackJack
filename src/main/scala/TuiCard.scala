import Decks.Rank._
import scala.collection.mutable.ArrayBuffer
import Decks.Suite._
import Decks.Card
import Decks.Deck
import scala.collection.mutable.Map

class TuiCard {




  // Define the dimensions of the playing field
    val maxStingSize = 366 // nach vierter karte zeilenumbrunch
  // Method to generate Ascci cards and count the value
    var deck = new Deck
    val cards = ArrayBuffer[Card]()
    var deck2 = new Deck
    //deck = deck2.shuffle()

    def drawCard(): Unit = {
        var (c,deck2) = deck.pullFromTop()
        deck = deck2  
        cards += c
        print_ascii_cards(cards)
    }

    
    
    def print_ascii_cards(cards: ArrayBuffer[Card]): Unit = {

    val builder = new StringBuilder() 
    val builder2 = new StringBuilder()
    val cardTemplate = List(
      "==============",
      "|            |",
      "|  %-10s|",
      "|  %-10s|",
      "|            |",
      "=============="
    )

        for(a <- 0 to 5){
          for(card <-cards ){
              
            a match
              case 2 => builder2.append(String.format(cardTemplate.apply(2),card.rank)+" ")
              case 3 => builder2.append(String.format(cardTemplate.apply(3),card.suite)+" ")
              case _ => builder2.append(cardTemplate.apply(a)+" ")
            }
            builder2.append("\n")
          }
        
        

        
        var value = 0
        for(card <- cards){
            val b = card.rank.getRankValue
            value += b

        }
        builder2.append(String.format("\n\nValue: %d\n\n",value))
        builder2.append("==================================================")

  
      //println(builder)
      println(builder2)
  }

// vertikaler builder fals erwünscht
// for(card <- cards){
        //     builder.append(cardTemplate.apply(0))
        //     builder.append(cardTemplate.apply(1))
        //     builder.append(String.format(cardTemplate.apply(3),card.rank))
        //     builder.append(String.format(cardTemplate.apply(2),card.suite))
        //     builder.append(cardTemplate.apply(4))
        //     builder.append(cardTemplate.apply(5))
        // }
}
