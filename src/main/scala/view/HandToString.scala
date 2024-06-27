import util.Decks.{Rank,Suite,Card,Deck}
import scala.collection.mutable.ArrayBuffer
object HandToString {

    
    
    def print_ascii_cards(cards: ArrayBuffer[Card]): String = {

    val builder = new StringBuilder() 
    val builder2 = new StringBuilder()
    val cardTemplate2 = List(
      "==============",
      "|            |",
      "|  %-10s|",
      "|  %-10s|",
      "|            |",
      "=============="
    )
    val cardTemplate = List(
      "==============",
      "|  %-10s|",
      "|  %-10s|",
      "=============="
    )

        for(a <- 0 to 3){
          for(card <-cards ){
              
            a match
              case 1 => builder2.append(String.format(cardTemplate.apply(1),card.rank)+" ")
              case 2 => builder2.append(String.format(cardTemplate.apply(2),card.suite)+" ")
              case _ => builder2.append(cardTemplate.apply(a)+" ")
            }
            builder2.append("\n")
          }
        
        

        
        var value = 0
        for(card <- cards){
            val b = card.rank.getRankValue
            value += b

        }
        
      //println(builder)
      //Return builder
      builder2.toString()
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
