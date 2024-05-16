import Decks._

trait Command {
  def execute(): Unit
}

class HitCommand(player: Player, controller: Controller) extends Command {
  override def execute(): Unit = {
    val card = controller.drawNewCard()
    player.addCard(card)
  }
}

class StandCommand(controller: Controller) extends Command {
  override def execute(): Unit = {
    while (controller.evaluate.evaluateHand(controller.table.getDealerHand()) < 17) {
      val card = controller.drawNewCard()
      controller.table.addDealerHand(card)
    }
  }
}
