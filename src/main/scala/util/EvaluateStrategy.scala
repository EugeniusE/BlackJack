package util

import scala.collection.mutable.ArrayBuffer
import util.Decks.Card
import util.Decks.Rank._

trait EvaluationStrategy {
  def evaluateHand(hand: ArrayBuffer[Card]): Int
}



  class StandardEvaluationStrategy extends EvaluationStrategy {
  override def evaluateHand(hand: ArrayBuffer[Card]): Int = {
    var score = hand.map(_.rank.getRankValue).sum

    if (score > 21) {
      var aceCount = hand.count(_.rank == Ace)
      while (score > 21 && aceCount > 0) {
        score -= 10
        aceCount -= 1
      }
    }

    score
  } 
}
// es können andere Evaluierungsstrategien genutzt werden



// Asse werden immer als elf
  class HighStakes extends EvaluationStrategy {
  override def evaluateHand(hand: ArrayBuffer[Card]): Int = {
    var score = hand.map(_.rank.getRankValue).sum

    score
  } 
}

