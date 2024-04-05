@main def hello(): Unit =
  def msg = "I was compiled by Scala 3. :)"
  val app = new ScalaFXHelloWorld()
  app.start()
  println("Hello world!")
  println(msg)

 
// object PlayingField {
//   // Define the dimensions of the playing field
//   val width = 10
//   val height = 5
  
//   // Method to generate the string representation of the playing field
//   def generateField(): String = {
//     val builder = new StringBuilder()
    
//     for (y <- 0 until height) {
//       for (x <- 0 until width) {
        
//         builder.append('.')
//       }
      
//       builder.append('\n')
//     }
    
    
//     builder.toString()
//   }
//   //main calls generate field
//   def main(args: Array[String]): Unit = {
//     val fieldString = generateField()
//     println(fieldString)
//   }
//}

