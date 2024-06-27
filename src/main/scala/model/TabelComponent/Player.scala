package model
import scala.collection.mutable.ArrayBuffer
import util.Decks.Card
class Player (private var money:Int,val name :String){
    
    private val hand = new ArrayBuffer[Card]

    def addCard(card:Card):Unit ={
        hand.addOne(card)
    }
    def removeCard(card:Card):Unit = {
      val index = hand.indexOf(card)
      hand.remove(index)
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
    def setPlayerMoney(amount:Int):Unit = {money = amount}
}
