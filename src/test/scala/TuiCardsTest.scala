import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import Decks.Rank._
import Decks.Suite._
import Decks.Deck
import Decks.Card
import scala.collection.mutable.ArrayBuffer

class TuiCardTest extends AnyWordSpec with Matchers {
  
  "A TuiCard" should {
    "be initialized with an empty card list and a non-null deck" in {
      val tuiCard = new TuiCard
      tuiCard.cards shouldBe empty  // Die Kartenliste sollte anfangs leer sein
      tuiCard.deck should not be null  // Das Deck sollte initialisiert sein
    }

    "have one card in hand after drawing a card from the deck" in {
      val tuiCard = new TuiCard
      tuiCard.drawCard()  // Eine Karte vom Deck ziehen
      tuiCard.cards should have size 1  // Jetzt sollte eine Karte im Blatt sein
    }

    "produce correct ASCII output for one card" in {
      val tuiCard = new TuiCard
      tuiCard.deck = new Deck()  // Deck zurücksetzen, um die Ausgabe zu kontrollieren
      tuiCard.drawCard()

      val expectedOutput = """
==============
|            |
|  Two       |
|  Spade     |
|            |
==============
Value: 2

=================================================="""
      tuiCard.cards.clear()
      tuiCard.cards += Card(Two, Spade)  // Ein einzelnes Ass der Herzen direkt zu den Karten hinzufügen
      val output = new java.io.ByteArrayOutputStream()
      Console.withOut(output) {
        tuiCard.print_ascii_cards(tuiCard.cards)
      }
      output.toString.trim should include(expectedOutput.trim)
    }

    "calculate the total value correctly with multiple cards" in {
      val tuiCard = new TuiCard
      tuiCard.cards += Card(Ace, Heart)
      tuiCard.cards += Card(King, Spade)
      val expectedValue = 21  // Ass (11) + König (10)
      tuiCard.cards.foreach { card =>
        card.rank.getRankValue should be > 0  // Überprüfen, dass die Werte korrekt zugeordnet sind
      }
      val totalValue = tuiCard.cards.map(card => card.rank.getRankValue).sum
      totalValue shouldEqual expectedValue
    }
  }
}
