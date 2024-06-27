package util
import util.Decks.{Rank,Suite,Card,Deck}
import model._
import play.api.libs.json._
import java.nio.file.{Files, Paths}
import scala.collection.mutable.ArrayBuffer
import com.google.inject.Inject
import com.google.inject.Guice
import control.Ergebnis
import java.io.{BufferedWriter, File, FileWriter, PrintWriter}

class JSONFileIO @Inject()(game: GameType) extends FileIOInterface {

  // Define custom JSON writers for Card and Deck
  implicit val cardWrites: Writes[Card] = new Writes[Card] {
    def writes(card: Card): JsValue = Json.obj(
      "rank" -> card.rank.toString,
      "suite" -> card.suite.toString
    )
  }

  implicit val cardReads: Reads[Card] = new Reads[Card] {
    def reads(json: JsValue): JsResult[Card] = {
      for {
        rank <- (json \ "rank").validate[String]
        suite <- (json \ "suite").validate[String]
      } yield Card(Rank.withName(rank), Suite.withName(suite))
    }
  }

  implicit val deckWrites: Writes[Deck] = new Writes[Deck] {
    def writes(deck: Deck): JsValue = Json.obj(
      "cards" -> deck.getCards.map(Json.toJson[Card])
    )
  }

  implicit val deckReads: Reads[Deck] = new Reads[Deck] {
    def reads(json: JsValue): JsResult[Deck] = {
      (json \ "cards").validate[Seq[Card]].map(cards => new Deck(cards.toList))
    }
  }

  // Define format for ArrayBuffer[Card]
  implicit val arrayBufferCardFormat: Format[ArrayBuffer[Card]] = Format(
    Reads.seq[Card].map(_.to(ArrayBuffer)),
    Writes.seq[Card].contramap(_.toSeq)
  )

  override def load: TableInterface = {
    val source = scala.io.Source.fromFile("game.json")
    try {
      val json = Json.parse(source.mkString)
      jsonToTableInterface(json)
    } finally {
      source.close()
    }
  }

  override def save(gameState: TableInterface): Unit = {
    val json = tableInterfaceToJson(gameState)
    val writer = new PrintWriter(new File("game.json"))
    try {
      writer.write(Json.prettyPrint(json))
    } finally {
      writer.close()
    }
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
    val outcome = (json \ "outcome").as[String]

    val table = new Table(game) // Implement the Table class based on TableInterface
    table.setDeck(deck)
    dealerHand.foreach(table.addDealerHand)
    playerHand.foreach(table.addPlayerHand)
    table.setPlayerMoney(playerMoney)
    table.setBet(bet)
    table.setOutcome(sErgebnisToErgebnis(outcome.toString()))
    table
  }

  def sErgebnisToErgebnis(s: String): Ergebnis = s match {
    case "PlayerWin" => Ergebnis.PlayerWin
    case "DealerWin" => Ergebnis.DealerWin
    case "Draw" => Ergebnis.Draw
    case "Undecided" => Ergebnis.Undecided
  }
}