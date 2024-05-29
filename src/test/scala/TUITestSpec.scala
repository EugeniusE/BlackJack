import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import java.io.{ByteArrayInputStream, ByteArrayOutputStream, PrintStream}

class TUITestSpec extends AnyWordSpec with Matchers {
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
      output should include("Falsche Eingabe.")
      output should include("Bitte Hit (h), Stand (s), Undo (u), Redo (r), oder Quit (q)")
    }
    "handle s" in {
      val in = new ByteArrayInputStream("s\nq\n".getBytes)
      val out = new ByteArrayOutputStream()
      Console.withIn(in) {
        Console.withOut(new PrintStream(out)) {
          val tui = new TUI()
          tui.start()
        }
      }
      val output = out.toString
      output should include("Weiterspielen? (y), Undo (u), Redo (r), oder Quit (q)")
    }
    "handle no input" in {
      val in = new ByteArrayInputStream("s\n\n\nq\n".getBytes)
      val out = new ByteArrayOutputStream()
      Console.withIn(in) {
        Console.withOut(new PrintStream(out)) {
          val tui = new TUI()
          tui.start()
        }
      }
      val output = out.toString
      output should include("Keine Eingabe.")
    }
    "handle h  input" in {
      val in = new ByteArrayInputStream("h\nh\nh\nh\nh\nh\nh\nq\n".getBytes)
      val out = new ByteArrayOutputStream()
      Console.withIn(in) {
        Console.withOut(new PrintStream(out)) {
          val tui = new TUI()
          tui.start()
        }
      }
      val output = out.toString
      output should include("Spieler Verloren")
    }
    "handle r input first" in {
      val in = new ByteArrayInputStream("r\nu\nq\n".getBytes)
      val out = new ByteArrayOutputStream()
      Console.withIn(in) {
        Console.withOut(new PrintStream(out)) {
          val tui = new TUI()
          tui.start()
        }
      }
      val output = out.toString
      output should include("No commands to redo")
      output should include("No commands to undo")
    }


    // "process 's' input correctly, indicating dealer's turn" in {
    //   val in = new ByteArrayInputStream("s\nq\n".getBytes)
    //   val out = new ByteArrayOutputStream()
    //   Console.withIn(in) {
    //     Console.withOut(new PrintStream(out)) {
    //       val tui = new TUI()
    //       tui.start()
    //     }
    //   }
    //   val output = out.toString
    //   output should include("Dealer ist am Zug .......")
    // }




  }
}
