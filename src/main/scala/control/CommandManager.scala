package control



import scala.collection.mutable.Stack
import scala.util.Success
import scala.util.Failure

class CommandManager {
  val executedCommands: Stack[Command] = Stack()
  val undoneCommands    : Stack[Command] = Stack()



  def executeCommand(command: Command): Unit = {
    command.execute() match{
        case Success(value) => 
            executedCommands.push(command)
            undoneCommands.clear()
        case Failure(exception) => 
            println(s"Error executing command: ${exception.getMessage}")
            // error command nicht erfolgreich 

    }
  }

  def undoLastCommand(): Unit = {
    if(executedCommands.nonEmpty){
        val lastCommand = executedCommands.pop()
        lastCommand.undo() match{
            case Success(value) => 
                undoneCommands.push(lastCommand)
            case Failure(exception) => 
                println(s"error undo command failure undoing : ${exception.getMessage()}")
        } 
        }   else
            println("No commands to undo")
    }
    def redoLastUndoneCommand(): Unit = {
        if (undoneCommands.nonEmpty) {
            val lastUndoneCommand = undoneCommands.pop()
            lastUndoneCommand.execute() match {
            case scala.util.Success(_) => executedCommands.push(lastUndoneCommand)
            case scala.util.Failure(exception) => println(s"Error redoing command: ${exception.getMessage}")
            undoneCommands.push(lastUndoneCommand) // Push the failed command back to undoneCommands
            }
        } else 
            {
                println("No commands to redo")
            }
    }

  
}
