

import net.codingwell.scalaguice.ScalaModule
import com.google.inject.name.Names
import com.google.inject.{AbstractModule, Provides}

class BlackJackModule(game: GameType) extends  AbstractModule with ScalaModule{

    override def  configure(): Unit = {
        bind(classOf[ControllerInterface]).to(classOf[Controller])
        bind(classOf[TableInterface]).to(classOf[Table])
    }
    @Provides
    def provideGameType(): GameType = game

    @Provides
    def provideController(game: GameType): Controller = new Controller(game)

    @Provides
    def provideTable(game: GameType): Table = new Table(game)
}
  


