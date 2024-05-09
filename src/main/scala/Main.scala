import scala.collection.mutable.ArrayBuffer
import Decks.Card
import Decks.Rank._
import Decks.Suite._
object Main {
  def main(args: Array[String]): Unit = {
    // Call the generateField method from the TuiCard object

    val controller = new Controller()
    val tui = new TUI()
    tui.start()
    // val nums = Seq(1,2,3,4,5,6)
    // for( a <- 0 to 0){
    //      tuiCard.drawCard();
    //   }
    // val tui = new TUI
    // tui.start()
    // Print or use the generated field string
    //println(field)
  }
}