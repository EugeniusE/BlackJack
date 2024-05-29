
import scala.collection.mutable.ArrayBuffer
import Decks.Card
class Player (private var money:Int,val name :String){
    
    val hand = new ArrayBuffer[Card]

    def addCard(card:Card):Unit ={
        hand.addOne(card)
    }
    def clearHand(): Unit = hand.clear()
    
    def getHand(): ArrayBuffer[Card] = hand

    def getMoney: Int = money
  
    def increaseMoney(amount: Int): Unit = {
    money += amount
     }
  
    def decreaseMoney(amount: Int): Unit = {
      money -= amount
    }
}
