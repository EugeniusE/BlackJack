import play.api.libs.json._
import java.nio.file.{Files, Paths}
import scala.collection.mutable.ArrayBuffer
import Decks._

class JSONFileIO extends FileIOInterface {

  implicit val cardFormat: Format[Card] = Json.format[Card]
  implicit val deckFormat: Format[Deck] = Json.format[Deck]

  override def load: TableInterface = {
    val source = Files.readString(Paths.get("game.json"))
    val json = Json.parse(source)
    jsonToTableInterface(json)
  }

  override def save(gameState: TableInterface): Unit = {
    val json = tableInterfaceToJson(gameState)
    Files.writeString(Paths.get("game.json"), Json.prettyPrint(json))
  }

  private def tableInterfaceToJson(table: TableInterface): JsValue = {
    Json.obj(
      "dealerHand" -> Json.toJson(table.getDealerHand()),
      "playerHand" -> Json.toJson(table.getPlayerHand()),
      "deck" -> Json.toJson(table.getDeck()),
      "playerMoney" -> JsNumber(table.getPlayerMoney()),
      "bet" -> JsNumber(table.getBet()),
      "outcome" -> JsString(table.getOutcome().toString)
    )
  }

  private def jsonToTableInterface(json: JsValue): TableInterface = {
    val dealerHand = (json \ "dealerHand").as[ArrayBuffer[Card]]
    val playerHand = (json \ "playerHand").as[ArrayBuffer[Card]]
    val deck = (json \ "deck").as[Deck]
    val playerMoney = (json \ "playerMoney").as[Int]
    val bet = (json \ "bet").as[Int]
    val outcome = Ergebnis.valueOf((json \ "outcome").as[String]).get

    val table = new Table() // You need to implement the Table class based on TableInterface
    table.setDeck(deck)
    dealerHand.foreach(table.addDealerHand)
    playerHand.foreach(table.addPlayerHand)
    table.setPlayerMoney(playerMoney)
    table.setBet(bet)
    table.setOutcome(outcome)
    table
  }
}
