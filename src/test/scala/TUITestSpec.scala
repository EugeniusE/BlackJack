import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import java.io.{ByteArrayInputStream, ByteArrayOutputStream, PrintStream}
import scala.io.StdIn.readLine

class TUITestSpec extends AnyWordSpec with Matchers {

  val evalStrat =
    new StandardEvaluationStrategy // Different evaluation strategies can be chosen here
  // val controller = new Controller(evalStrat)

  "TUI" should {
    "handle incorrect input and prompt again" in {
      val in = new ByteArrayInputStream("x\nq\n".getBytes)
      val out = new ByteArrayOutputStream()
      Console.withIn(in) {
        Console.withOut(new PrintStream(out)) {
          mockMain()
        }
      }
      val output = out.toString
      output should include("Falsche Eingabe.")
      output should include(
        "Bitte Hit (h), Stand (s), Undo (u), Redo (r), oder Quit (q)"
      )
    }
    "handle s" in {
      val in = new ByteArrayInputStream("s\nq\n".getBytes)
      val out = new ByteArrayOutputStream()
      Console.withIn(in) {
        Console.withOut(new PrintStream(out)) {
         mockMain()
        }
      }
      val output = out.toString
      output should include(
        "Weiterspielen? (y), Undo (u), Redo (r), oder Quit (q)"
      )
    }
    "handle no input" in {
      val in = new ByteArrayInputStream("\ns\n\n\nq\n".getBytes)
      val out = new ByteArrayOutputStream()
      Console.withIn(in) {
        Console.withOut(new PrintStream(out)) {
          mockMain()
        }
      }
      val output = out.toString
      output should include("Keine Eingabe!")
    }
    "handle h  input" in {
      val in = new ByteArrayInputStream("h\nh\nh\nh\nh\nh\nh\nq\n".getBytes)
      val out = new ByteArrayOutputStream()
      Console.withIn(in) {
        Console.withOut(new PrintStream(out)) {
          mockMain()
        }
      }
      val output = out.toString
      output should include("Dealer hat gewonnen")
    }
    "handle r input first" in {
      val in = new ByteArrayInputStream("r\nu\nq\n".getBytes)
      val out = new ByteArrayOutputStream()
      Console.withIn(in) {
        Console.withOut(new PrintStream(out)) {
          mockMain()
        }
      }
      val output = out.toString
      output should include("No commands to redo")
      output should include("No commands to undo")
    }

  }
  def mockMain() :  Unit = {
  var input = ""
  val evalStrat =
  new StandardEvaluationStrategy // Different evaluation strategies can be chosen here
  // val controller = new Controller(evalStrat)
  val gameBuilder = new StandardGameBuilder
  gameBuilder.setPlayer("Spieler1", 500)
  val game = gameBuilder.build()
  val controller = new Controller(game)
  val tui = new TUI(controller)
  controller.add(tui)
  controller.newGame()
  while(input!= "q"){
     input = readLine()
     if(input!= "q")
        tui.getInputAndLoop(input)
    }
  }
}
