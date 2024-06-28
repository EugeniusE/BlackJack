package util


import model._
import com.google.inject.Inject



class GameType (
  val evalStrat : EvaluationStrategy,

  val deckFactoryType : FactoryType,

  val player: Player
)
  
trait GameBuilder{
  def setEvaluationStrategy(stategy: EvaluationStrategy): GameBuilder

  def setDeckFactoryType(deckFactoryType:FactoryType):GameBuilder

  def setPlayer(name:String,betrag:Int):GameBuilder

  def build():GameType

}

class StandardGameBuilder extends GameBuilder {
  private var evaluationStrategy: EvaluationStrategy = new StandardEvaluationStrategy
  private var deckFactoryType: FactoryType = FactoryType.StandartDeck
  private var player: Player = new Player(500,"DefaultPlayer")

  override def setEvaluationStrategy(strategy: EvaluationStrategy): GameBuilder = {
    this.evaluationStrategy = strategy
    this
  }

  override def setDeckFactoryType(deckFactoryType: FactoryType): GameBuilder = {
    this.deckFactoryType = deckFactoryType
    this
  }

  override def setPlayer(name:String,betrag:Int): GameBuilder = {
    this.player = new Player(betrag,name)
    this
  }

  override def build(): GameType = {
    new GameType(evaluationStrategy, deckFactoryType, player)
  }
}

