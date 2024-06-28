import util.Decks.{Rank, Suite, Card, Deck}
import scala.collection.mutable.ArrayBuffer
import scalafx.scene.layout.{HBox, VBox, StackPane}
import scalafx.scene.image.{Image, ImageView}
import java.io.FileInputStream
import scalafx.scene.control.ContentDisplay

import scalafx.scene.control.{Button, Label}
import scalafx.geometry.Insets

object HandToString {
  val buttonImage = new Image(
    new FileInputStream("src/main/scala/resources/ButtonBackground.png")
  )

  def print_ascii_cards(cards: ArrayBuffer[Card]): String = {

    val builder = new StringBuilder()
    val builder2 = new StringBuilder()
    val cardTemplate2 = List(
      "==============",
      "|            |",
      "|  %-10s|",
      "|  %-10s|",
      "|            |",
      "=============="
    )
    val cardTemplate = List(
      "==============",
      "|  %-10s|",
      "|  %-10s|",
      "=============="
    )

    for (a <- 0 to 3) {
      for (card <- cards) {

        a match
          case 1 =>
            builder2.append(
              String.format(cardTemplate.apply(1), card.rank) + " "
            )
          case 2 =>
            builder2.append(
              String.format(cardTemplate.apply(2), card.suite) + " "
            )
          case _ => builder2.append(cardTemplate.apply(a) + " ")
      }
      builder2.append("\n")
    }

    var value = 0
    for (card <- cards) {
      val b = card.rank.getRankValue
      value += b

    }

    // println(builder)
    // Return builder
    builder2.toString()
  }

  def createNewButton(buttonText: String): Button = {
  val b = new Button {
    graphic = new StackPane {
      children = new ImageView(buttonImage) {
        fitWidth = 140
        fitHeight = 60
        preserveRatio = true
        smooth = true
      }
      children.add(new Label(buttonText) {
        style = "-fx-text-fill: white; -fx-font-weight: bold;"
      })
    }
    contentDisplay = ContentDisplay.GraphicOnly
    padding = Insets(0)
    style = """
      -fx-border-color: transparent;
      -fx-border-width: 2px;
    """
  }

  // Adding the hover effect
  b.setOnMouseEntered(_ => b.setStyle("""
      -fx-border-color: red;
      -fx-border-width: 2px;
    """))
  b.setOnMouseExited(_ => b.setStyle("""
      -fx-border-color: transparent;
      -fx-border-width: 2px;
    """))

  b
}

// vertikaler builder fals erwünscht
// for(card <- cards){
  //     builder.append(cardTemplate.apply(0))
  //     builder.append(cardTemplate.apply(1))
  //     builder.append(String.format(cardTemplate.apply(3),card.rank))
  //     builder.append(String.format(cardTemplate.apply(2),card.suite))
  //     builder.append(cardTemplate.apply(4))
  //     builder.append(cardTemplate.apply(5))
  // }
}
