import Decks._
import scala.util.Try
import scala.collection.mutable.ArrayBuffer

trait Command {
  def execute(): Try[Unit]
  def undo(): Try[Unit]
}

class HitCommand(controller: Controller) extends Command {
  var card : Option[Card] = None
  var prevState: Ergebnis = controller.getOutcome()
  override def execute(): Try[Unit] = Try{
    card  = Some(controller.drawNewCard())
    controller.addPlayerHand(card.get)

    if (controller.evaluateHand(controller.getPlayerHand()) > 21) {
     controller.setOutcome(Ergebnis.DealerWin) 
    }
    
  }

  override def undo(): Try[Unit] = Try{

    card match{
      case Some(card) =>
        // val index = controller.getPlayerHand().indexOf(card)
        // controller.table.player.hand.remove(index)
        controller.removePlayerHand(card)
      case None => //keine karte entfernen
    }
    card = None
    controller.setOutcome(prevState)
  }
  
}

class StandCommand(controller: Controller) extends Command {
  private var dealerInitHand: Option[List[Card]] = None
  var prevState: Ergebnis = controller.getOutcome()
  override def execute(): Try[Unit] = Try{
    dealerInitHand = Some(controller.getDealerHand().toList)

    while (controller.evaluateHand(controller.table.getDealerHand()) < 17) {
      val card = controller.drawNewCard()
      controller.addDealerHand(card)
    }

    val dealerScore = controller.evaluateHand(controller.getDealerHand())
    val playerScore = controller.evaluateHand(controller.getPlayerHand())
    

    if (dealerScore > 21 || dealerScore < playerScore) {
      controller.setOutcome(Ergebnis.PlayerWin) 
    } else if (dealerScore == playerScore) {
     controller.setOutcome(Ergebnis.Draw) 
    } else {
      controller.setOutcome(Ergebnis.DealerWin)
    }

  }

  override def undo(): Try[Unit] = Try{
    dealerInitHand match{
      case Some(value) =>
         controller.clearDealerhand()
         for(card <- value){
          controller.addDealerHand(card)
         }

      case None => //kein undo 
    }
    dealerInitHand = None
    controller.setOutcome(prevState)
  }
}
