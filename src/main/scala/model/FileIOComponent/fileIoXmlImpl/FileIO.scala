import scala.xml.{XML, Elem, NodeSeq, PrettyPrinter}
import java.nio.file.{Files, Paths}
import scala.collection.mutable.ArrayBuffer
import Decks._

class XMLFileIO extends FileIOInterface {

  override def load: TableInterface = {
    val file = XML.loadFile("game.xml")
    xmlToTableInterface(file)
  }

  override def save(gameState: TableInterface): Unit = {
    val xml = tableInterfaceToXml(gameState)
    val prettyPrinter = new PrettyPrinter(120, 4)
    val formattedXml = prettyPrinter.format(xml)
    Files.writeString(Paths.get("game.xml"), formattedXml)
  }

  private def tableInterfaceToXml(table: TableInterface): Elem = {
    <tableState>
      <dealerHand>{ table.getDealerHand().map(cardToXml) }</dealerHand>
      <playerHand>{ table.getPlayerHand().map(cardToXml) }</playerHand>
      <deck>{ deckToXml(table.getDeck()) }</deck>
      <playerMoney>{ table.getPlayerMoney() }</playerMoney>
      <bet>{ table.getBet() }</bet>
      <outcome>{ table.getOutcome().toString }</outcome>
    </tableState>
  }

  private def xmlToTableInterface(xml: Elem): TableInterface = {
    val dealerHand = (xml \ "dealerHand" \ "card").map(xmlToCard)
    val playerHand = (xml \ "playerHand" \ "card").map(xmlToCard)
    val deck = xmlToDeck((xml \ "deck").head)
    val playerMoney = (xml \ "playerMoney").text.toInt
    val bet = (xml \ "bet").text.toInt
    val outcome = Ergebnis.valueOf((xml \ "outcome").text).get

    val table = new Table() // Implement the Table class based on TableInterface
    table.setDeck(deck)
    dealerHand.foreach(table.addDealerHand)
    playerHand.foreach(table.addPlayerHand)
    table.setPlayerMoney(playerMoney)
    table.setBet(bet)
    table.setOutcome(outcome)
    table
  }

  private def cardToXml(card: Card): Elem = {
    <card>
      <rank>{ card.rank.toString }</rank>
      <suite>{ card.suite.toString }</suite>
    </card>
  }

  private def xmlToCard(node: NodeSeq): Card = {
    val rank = Rank.valueOf((node \ "rank").text).get
    val suite = Suite.valueOf((node \ "suite").text).get
    Card(rank, suite)
  }

  private def deckToXml(deck: Deck): Elem = {
    <deck>
      { deck.getCards.map(cardToXml) }
    </deck>
  }

  private def xmlToDeck(node: NodeSeq): Deck = {
    val cards = (node \ "card").map(xmlToCard).toList
    new Deck(cards)
  }
}
