val s = 5+5 
val card =  "=========\n" +
            "|       |\n" +
            "|       |\n" + 
            "|       |\n" +
            "=========\n" 
//  =========  ========= ========= ========= ========= =========        
//  | Ace   |  | Ace   | | Ace   | | Ace   | | Ace   | | Ace   |        
//  | Spades|  | Spades| | Spades| | Spades| | Spades| | Spades|        valueOfHand
//  |       |  |       | |       | |       | |       | |       |        
//  =========  ========= ========= ========= ========= =========
  val evalStrat =
  new EvaluationStrategy.StandardEvaluationStrategy // Different evaluation strategies can be chosen here
  // val controller = new Controller(evalStrat)
  val gameBuilder = new StandardGameBuilder
  gameBuilder.setPlayer("Spieler1", 500)
  val game = gameBuilder.build()
  val controller = new Controller(game)

   private def updateCardImages(): Unit = {
    playerCardImages.children.clear()
    dealerCardImages.children.clear()

    controller.table.player.hand.foreach { card =>
      val cardImage = new ImageView(new Image(s"src/main/scala/resources/cards2.0${card.rank}${card.suite.toString().charAt(1)}.png")) {
        fitHeight = 100
        fitWidth = 70
      }
      playerCardImages.children.add(cardImage)
    }

    controller.table.getDealerHand().foreach { card =>
      val cardImage = new ImageView(new Image(s"src/main/scala/resources/cards2.0${card.rank}${card.suite.toString().charAt(1)}.png")) {
        fitHeight = 100
        fitWidth = 70
      }
      dealerCardImages.children.add(cardImage)
    }
  }

  private def updateScores(): Unit = {
    playerScoreLabel.text = s"Player Score: ${controller.game.evalStrat.evaluateHand(controller.table.player.hand)}"
    dealerScoreLabel.text = s"Dealer Score: ${controller.game.evalStrat.evaluateHand(controller.table.getDealerHand())}"
  }
