import scala.io.StdIn.readLine

class TUI {

 def start(): Unit = {
    val hand = new TuiCard()
    hand.drawCard()
    getInputAndLoop(hand)
  }

  def getInputAndLoop(hand: TuiCard): Unit = {
    println("Bitte Hit (h) or stand (s) beenden (q)")
    val chars = readLine().toCharArray()

    if (chars.length > 1) {
      println("falsche eingabe")
      getInputAndLoop(hand)
    }

    val eingabe = chars(0).toLower match {
      case 'q' => None
      case 'h' =>
        hand.drawCard()
        getInputAndLoop(hand)
      case 's' =>
      // Implement dealer's turn here
    }
  }
}
