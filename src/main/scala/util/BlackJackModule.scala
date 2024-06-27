import net.codingwell.scalaguice.ScalaModule
import com.google.inject.name.Names
import com.google.inject.{AbstractModule, Provides}
import Decks.FactoryType
import model._
import control.contollerComponent._
import TableComponent.Table
import FileIOComponent.fileIoJsonImpl.JSONFileIO
import FileIOComponent.fileIoXmlImpl.XMLFileIO

class BlackJackModule(game: GameType) extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind(classOf[ControllerInterface]).to(classOf[Controller])
    bind(classOf[TableInterface]).to(classOf[Table])
    bind(classOf[FileIOInterface]).annotatedWith(Names.named("XML")).to(classOf[XMLFileIO])
    bind(classOf[FileIOInterface]).annotatedWith(Names.named("JSON")).to(classOf[JSONFileIO])
  }

  @Provides
  def provideGameType(): GameType = game

  @Provides
  def provideController(game: GameType, @Named("JSON") fileIO: FileIOInterface): Controller = new Controller(game, fileIO)

  @Provides
  def provideTable(game: GameType): Table = new Table(game)
}

object default {
  given tableI(using game: GameType): TableInterface = Table(game)
}
