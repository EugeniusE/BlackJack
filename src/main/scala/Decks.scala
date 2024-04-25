import scala.util.Random
import Decks.Rank._
import Decks.Suite._
object Decks  {

  
  enum Suite:
    case Spade, Heart, Club, Diamond
    def fromString(str:String) = Suite.valueOf(str)
  enum Rank:
    case Two,Three,Four,Five,Six,Seven,Eight,Nine,Ten,Jack,Queen,King,Ace
    def fromString(str:String) = Suite.valueOf(str)

  private val suites = List(Spade, Heart, Club, Diamond)
  private val ranks = List(Two,Three,Four,Five,Six,Seven,Eight,Nine,Ten,Jack,Queen,King,Ace)

  //Methods for the deck
  case class Card(rank: Rank, suite: Suite)

  class Deck(cards: List[Card] = for (r <- ranks; s <- suites) yield Card(r, s)) {

    def shuffle() = new Deck(Random.shuffle(cards))

    def pullFromTop(): (Card, Deck) = (cards.head, new Deck(cards.tail))

    def addToTop(card: Card) = new Deck(card :: cards)

    def addToTop(cardsToAdd: List[Card]) = new Deck(cardsToAdd ::: cards)

    def size  : Int = cards.length
  }
}
