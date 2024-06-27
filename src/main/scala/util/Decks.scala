package util
import play.api.libs.json._
import scala.util.Random
import Decks.Rank._
import Decks.Suite._
object Decks {

  // enum Suite:
  //   case Spade, Heart, Club, Diamond
  // enum Rank:
  //   case Two,Three,Four,Five,Six,Seven,Eight,Nine,Ten,Jack,Queen,King,Ace

  object Rank extends Enumeration {
  type Rank = Value
  val Two, Three, Four, Five, Six, Seven, Eight, Nine, Ten, Jack, Queen, King, Ace = Value

  implicit class RankOps(rank: Rank) {
    def getRankValue: Int = rank match {
      case Two   => 2
      case Three => 3
      case Four  => 4
      case Five  => 5
      case Six   => 6
      case Seven => 7
      case Eight => 8
      case Nine  => 9
      case Ten   => 10
      case Jack  => 10
      case Queen => 10
      case King  => 10
      case Ace   => 11
    }
  }

  def withNameOpt(name: String): Option[Value] =
    values.find(_.toString == name)
}
  object Suite extends Enumeration {
    type Suite = Value
    val Spade, Heart, Club, Diamond = Value

    def withNameOpt(name: String): Option[Value] =
      values.find(_.toString == name)
  }
  // def getRankValue: Int = this match {
  //   case Two   => 2
  //   case Three => 3
  //   case Four  => 4
  //   case Five  => 5
  //   case Six   => 6
  //   case Seven => 7
  //   case Eight => 8
  //   case Nine  => 9
  //   case Ten   => 10
  //   case Jack  => 10
  //   case Queen => 10
  //   case King  => 10
  //   case Ace   => 11
  // }

  private val suites = List(Spade, Heart, Club, Diamond)
  private val ranks = List(
    Two,
    Three,
    Four,
    Five,
    Six,
    Seven,
    Eight,
    Nine,
    Ten,
    Jack,
    Queen,
    King,
    Ace
  )

  // Methods for the deck
  case class Card(rank: Rank, suite: Suite)
  class Deck(cards: List[Card] = for (
    r <- ranks; s <- suites
  ) yield Card(r, s)) {

    def shuffle() = new Deck(Random.shuffle(cards))

    def pullFromTop(): (Card, Deck) = (cards.head, new Deck(cards.tail))

    def addToTop(card: Card) = new Deck(card :: cards)

    def addToTop(cardsToAdd: List[Card]) = new Deck(cardsToAdd ::: cards)

    def size: Int = cards.length

    def getCards: List[Card] = cards
  }
}

trait DeckFactory {
  def createDeck(): Decks.Deck
}

private class StandardDeckFactory extends DeckFactory {
  override def createDeck(): Decks.Deck = { new Decks.Deck().shuffle() }
}

private class SixDeckGame extends DeckFactory {
  override def createDeck(): Decks.Deck = {
    val sixDecks: List[Decks.Deck] = List.fill(5)(new Decks.Deck())
    var finalDeck: Decks.Deck = new Decks.Deck()

    for (deck <- sixDecks) {
      finalDeck = finalDeck.addToTop(deck.getCards)
    }
    finalDeck.shuffle()

  }
}

enum FactoryType:
  case SixDecks, StandartDeck

object DeckFactory {
  def apply(factoryKind: FactoryType) = factoryKind match {

    case FactoryType.SixDecks     => new SixDeckGame().createDeck()
    case FactoryType.StandartDeck => new StandardDeckFactory().createDeck()

  }

}
def cardPath(card: Decks.Card): String = {
  var path: String = ""
  card.rank match {
    case Queen => path = path.appended('Q')
    case Ace   => path = path.appended('A')
    case King  => path = path.appended('K')
    case Jack  => path = path.appended('J')
    case _     => path = path + s"${card.rank.getRankValue}"
  }
  path = path + s"${card.suite.toString().charAt(0)}"
  path
}
