class Background {
  
}
// import javafx.application.Application
// import javafx.scene.{Scene, Group}
// import javafx.scene.image.{Image, ImageView}
// import javafx.scene.layout.{StackPane, Background, BackgroundFill}
// import javafx.scene.paint.Color
// import javafx.stage.Stage

object DisplayImagesWithBackground extends Application {
  override def start(primaryStage: Stage): Unit = {
    // Load background image
    val backgroundImage = new Image("file:path/to/background.png")

    // Load PNG images
    val image1 = new Image("ressources/cards/2_ofDiamonds.png")
    val image2 = new Image("ressources/cards/2_of_Clubs.png")

    // Create ImageView objects to display images
    val imageView1 = new ImageView(image1)
    val imageView2 = new ImageView(image2)

    // Create a Group to hold ImageView objects
    val group = new Group(imageView1, imageView2)

    // Create a StackPane to add the background
    val stackPane = new StackPane(group)

    // Add the background image to the StackPane
    stackPane.setBackground(new Background(Array(new BackgroundFill(Color.LIGHTGRAY, null, null))))

    // Set the size of the background image to fit the scene
    stackPane.setPrefSize(backgroundImage.getWidth, backgroundImage.getHeight)

    // Create a Scene with the StackPane as the root
    val scene = new Scene(stackPane)

    // Set the scene to the stage and show it
    primaryStage.setScene(scene)
    primaryStage.setTitle("Display Images with Background")
    primaryStage.show()
  }
}
