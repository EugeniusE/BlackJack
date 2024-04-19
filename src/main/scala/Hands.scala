
object Hands {
    class Hand(val cards: List[Decks.Card] = List()) {
        def addCard(card: Decks.Card): Hand = new Hand(card :: cards)
        
        def value(): Int = {
            val (total, aceCount) = cards.foldLeft((0, 0)) {
                case ((sum, aceCount), card) if card.rank == Decks.Ace => (sum + 11, aceCount + 1)
                case ((sum, aceCount), card) => (sum + cardValue(card.rank), aceCount)
            }
            if (total > 21 && aceCount > 0) total - 10 * aceCount else total // Adjust for Ace being 1 or 11
        }

        private def cardValue(rank: Decks.Rank): Int = rank match {
            case Decks.Two => 2
            case Decks.Three => 3
            case Decks.Four => 4
            case Decks.Five => 5
            case Decks.Six => 6
            case Decks.Seven => 7
            case Decks.Eight => 8
            case Decks.Nine => 9
            case Decks.Ten | Decks.Jack | Decks.Queen | Decks.King => 10
            case Decks.Ace => 11 // Initial value of Ace is 11, adjustment is handled in `value`
        }

        override def toString: String = cards.mkString(", ") + s" (Value: $value())"
    }

    case class Player(name: String, hand: Hand = new Hand())
}
