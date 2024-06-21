import Decks._
import scala.util.Try
import scala.collection.mutable.ArrayBuffer
import Main.controller

trait Command {
  def execute(): Try[Unit]
  def undo(): Try[Unit]
}

class HitCommand(controller: Controller) extends Command {
  var card: Option[Card] = None
  var prevState: Ergebnis = controller.getOutcome()
  override def execute(): Try[Unit] = Try {
    card = Some(controller.drawNewCard())
    controller.addPlayerHand(card.get)

    if (controller.evaluateHand(controller.getPlayerHand()) > 21) {
      controller.setOutcome(Ergebnis.DealerWin)
    }

  }

  override def undo(): Try[Unit] = Try {

    card match {
      case Some(card) =>
        // val index = controller.getPlayerHand().indexOf(card)
        // controller.table.player.hand.remove(index)
        controller.removePlayerHand(card)
      case None => // keine karte entfernen
    }
    card = None
    controller.setOutcome(prevState)
  }

}

class StandCommand(controller: Controller) extends Command {
  private var dealerInitHand: Option[List[Card]] = None
  private var prevPlayerMoney: Int = controller.getPlayerMoney()
  var prevState: Ergebnis = controller.getOutcome()
  override def execute(): Try[Unit] = Try {
    dealerInitHand = Some(controller.getDealerHand().toList)

    while (controller.evaluateHand(controller.getDealerHand()) < 17) {
      val card = controller.drawNewCard()
      controller.addDealerHand(card)
    }

    val dealerScore = controller.evaluateHand(controller.getDealerHand())
    val playerScore = controller.evaluateHand(controller.getPlayerHand())

    if (dealerScore > 21 || dealerScore < playerScore) {
      controller.setOutcome(Ergebnis.PlayerWin)
      controller.increasePlayerMoney(controller.getBet() * 2)
    } else if (dealerScore == playerScore) {
      controller.increasePlayerMoney(controller.getBet())
      controller.setOutcome(Ergebnis.Draw)
    } else {
      controller.setOutcome(Ergebnis.DealerWin)
    }

  }

  override def undo(): Try[Unit] = Try {
    dealerInitHand match {
      case Some(value) =>
        controller.clearDealerhand()
        for (card <- value) {
          controller.addDealerHand(card)
        }

      case None => // kein undo
    }
    dealerInitHand = None
    controller.setOutcome(prevState)
    if (controller.getPlayerMoney() > prevPlayerMoney)
      controller.decreasePlayerMoney(
        controller.getPlayerMoney() - prevPlayerMoney
      )
    else
      controller.increasePlayerMoney(
        prevPlayerMoney - controller.getPlayerMoney()
      )
  }
}

class BetCommand(controller: Controller, bA: Int) extends Command {
  private var betAmount = bA
  private var prevPlayerMoney = controller.getPlayerMoney()
  def execute(): Try[Unit] = Try {
    controller.setBet(betAmount)
    controller.decreasePlayerMoney(betAmount)
  }
  def undo(): Try[Unit] = Try {
    controller.increasePlayerMoney(betAmount)
    controller.clearBet()
  }
}
