import scala.collection.mutable.Stack
import scala.util.Success
import scala.util.Failure

class CommandManager {
  private val executedCommands: Stack[Command] = Stack()
  private val undoneCommands    : Stack[Command] = Stack()



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



        }  // else nichts zu tun
    }
    def redoLastUndoneCommand(): Unit = {
        if (undoneCommands.nonEmpty) {
            val lastUndoneCommand = undoneCommands.pop()
            lastUndoneCommand.execute() match {
            case scala.util.Success(_) => executedCommands.push(lastUndoneCommand)
            case scala.util.Failure(exception) => println(s"Error redoing command: ${exception.getMessage}")
            }
        } else 
            {
                println("No commands to redo")
            }
    }

  
}
