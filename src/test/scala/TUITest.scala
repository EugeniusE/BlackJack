import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import java.io.{ByteArrayInputStream, ByteArrayOutputStream, PrintStream}

class TUITest extends AnyWordSpec with Matchers {
  "TUI" should {
    "handle incorrect input and prompt again" in {
      val in = new ByteArrayInputStream("x\nq\n".getBytes)
      val out = new ByteArrayOutputStream()
      Console.withIn(in) {
        Console.withOut(new PrintStream(out)) {
          val tui = new TUI()
          tui.start()
        }
      }
      val output = out.toString
      output should include("falsche eingabe")
      output should include("Bitte Hit (h) or stand (s) beenden (q)")
    }

    "process 's' input correctly, indicating dealer's turn" in {
      val in = new ByteArrayInputStream("s\nq\n".getBytes)
      val out = new ByteArrayOutputStream()
      Console.withIn(in) {
        Console.withOut(new PrintStream(out)) {
          val tui = new TUI()
          tui.start()
        }
      }
      val output = out.toString
      output should include("Dealer ist am Zug .......TODO Implementierung")
    }
  }
}
