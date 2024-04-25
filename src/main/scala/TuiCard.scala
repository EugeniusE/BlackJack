import Decks.Rank._
import scala.collection.mutable.ArrayBuffer
import Decks.Suite._
import Decks.Card
import Decks.Deck
import scala.collection.mutable.Map
class TuiCard {




  // Define the dimensions of the playing field
  val valueMap = Map(
  Two -> 2,
  Three -> 3,
  Four -> 4,
  Five -> 5,
  Six -> 6,
  Seven -> 7,
  Eight -> 8,
  Nine -> 9,
  Ten -> 10,
  Jack -> 10,
  Queen -> 10,
  King -> 10,
  Ace -> 11
)
    var hand = List
    val height = 5
  // Method to generate Ascci cards and count the value
    var deck = new Deck
    val cards = ArrayBuffer[Card]()
    var deck2 = new Deck
    def drawCard(): Unit = {
        var (c,deck2) = deck.pullFromTop()
        deck = deck2  
        cards += c
        print_ascii_cards(cards)
    }

    
    
    def print_ascii_cards(cards: ArrayBuffer[Card]): Unit = {

    val builder = new StringBuilder() 
    val cardTemplate = List(
      "==============\n",
      "|            |\n",
      "|  %-10s|\n",
      "|  %-10s|\n",
      "|            |\n",
      "==============\n"
    )

        for(card <- cards){
            builder.append(cardTemplate.apply(0))
            builder.append(cardTemplate.apply(1))
            builder.append(String.format(cardTemplate.apply(3),card.rank))
            builder.append(String.format(cardTemplate.apply(2),card.suite))
            builder.append(cardTemplate.apply(4))
            builder.append(cardTemplate.apply(5))
        }

        
        var value = 0
        for(card <- cards){
            var b = valueMap.getOrElse(card.rank,0)
            value += b

        }
        builder.append(String.format("Value: %d\n\n",value))
        builder.append("==================================================")

    // Print multiple adjacent ASCII cards
     
    // for (line <- cardTemplate){
    //     builder.append(String.format(line,cards.apply(0).rank,cards.apply(0).suite)+"\n")
    // }
    // val card = builder
      // Add spacing between cards
      println(builder)
  }


//   def generateField(): String = {
//     val builder = new StringBuilder()
    
//     for (y <- 0 until height) {
//       for (x <- 0 until width) {
//         builder.append('.')
//       }
      
//       builder.append('\n')
//     }
    
//     builder.toString()
//   }
}
