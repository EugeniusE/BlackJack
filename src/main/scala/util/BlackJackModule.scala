package util

import net.codingwell.scalaguice.ScalaModule
import com.google.inject.name.Names
import com.google.inject.{AbstractModule, Provides}
import util.FactoryType
import util.FileIOInterface
import model._
import control._

class BlackJackModule(game: GameType) extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind(classOf[ControllerInterface]).to(classOf[Controller])
    bind(classOf[TableInterface]).to(classOf[Table])
    bind(classOf[FileIOInterface]).to(classOf[JSONFileIO])
    //bind(classOf[FileIOInterface]).to(classOf[XMLFileIO]) andere möglichkeit
  }

  @Provides
  def provideGameType(): GameType = game

  @Provides
  def provideController(game: GameType): Controller = new Controller(game)

  @Provides
  def provideTable(game: GameType): Table = new Table(game)
}

object default {
  given tableI(using game: GameType): TableInterface = Table(game)
}
