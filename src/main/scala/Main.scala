object Main {
  def main(args: Array[String]): Unit = {
    // Call the generateField method from the TuiCard object
    val tuiCard = new TuiCard()
    val nums = Seq(1,2,3,4,5,6)
    for( a <- 0 to 4){
         tuiCard.drawCard();
      }
    val tui = new TUI
    tui.start()
    // Print or use the generated field string
    //println(field)
  }
}