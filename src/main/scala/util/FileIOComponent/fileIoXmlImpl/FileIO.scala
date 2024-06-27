package util
import util._
import util.Decks._
import java.io._
import control.Ergebnis
import model._
import scala.xml.{XML, Elem, NodeSeq, PrettyPrinter}
import java.nio.file.{Files, Paths}
import scala.collection.mutable.ArrayBuffer
import com.google.inject.Inject
import com.google.inject.Guice
import java.nio.file.{Files, Paths}

class XMLFileIO @Inject()(game: GameType) extends FileIOInterface {

  override def load: TableInterface = {
    val file = scala.xml.XML.loadFile("game.xml")
    xmlToTableInterface(file)
  }

  override def save(gameState: TableInterface): Unit = {
    val xml = tableInterfaceToXml(gameState)
    val prettyPrinter = new scala.xml.PrettyPrinter(120, 4)
    val formattedXml = prettyPrinter.format(xml)

    val writer = new PrintWriter(new File("game.xml"))
    try {
      writer.write(formattedXml)
    } finally {
      writer.close()
    }
  }

  private def tableInterfaceToXml(table: TableInterface): scala.xml.Elem = {
    <tableState>
      <dealerHand>{ table.getDealerHand().map(cardToXml) }</dealerHand>
      <playerHand>{ table.getPlayerHand().map(cardToXml) }</playerHand>
      <deck>{ deckToXml(table.getDeck()) }</deck>
      <playerMoney>{ table.getPlayerMoney() }</playerMoney>
      <bet>{ table.getBet() }</bet>
      <outcome>{ table.getOutcome().toString }</outcome>
    </tableState>
  }

  private def xmlToTableInterface(xml: scala.xml.Elem): TableInterface = {
    val dealerHand = (xml \ "dealerHand" \ "card").map(xmlToCard)
    val playerHand = (xml \ "playerHand" \ "card").map(xmlToCard)
    val deck = xmlToDeck((xml \ "deck").head)
    val playerMoney = (xml \ "playerMoney").text.toInt
    val bet = (xml \ "bet").text.toInt
    val outcome = (xml \ "outcome").text

    val table = new Table(game) // Implement the Table class based on TableInterface
    table.setDeck(deck)
    dealerHand.foreach(table.addDealerHand)
    playerHand.foreach(table.addPlayerHand)
    table.setPlayerMoney(playerMoney)
    table.setBet(bet)
    table.setOutcome(sErgebnisToErgebnis(outcome))
    table
  }

  private def cardToXml(card: Card): scala.xml.Elem = {
    <card>
      <rank>{ card.rank.toString }</rank>
      <suite>{ card.suite.toString }</suite>
    </card>
  }

  private def xmlToCard(node: scala.xml.NodeSeq): Card = {
    val rankStr = (node \ "rank").text
    val suiteStr = (node \ "suite").text
    
    val rank = Rank.withName(rankStr)
    val suite = Suite.withName(suiteStr)
    
    Card(rank, suite)
  }

  private def deckToXml(deck: Deck): scala.xml.Elem = {
    <deck>
      { deck.getCards.map(cardToXml) }
    </deck>
  }

  private def xmlToDeck(node: scala.xml.NodeSeq): Deck = {
    val cards = (node \ "card").map(xmlToCard).toList
    new Deck(cards)
  }

  private def sErgebnisToErgebnis(s: String): Ergebnis = s match {
    case "PlayerWin" => Ergebnis.PlayerWin
    case "DealerWin" => Ergebnis.DealerWin
    case "Draw" => Ergebnis.Draw
    case "Undecided" => Ergebnis.Undecided
  }
}
