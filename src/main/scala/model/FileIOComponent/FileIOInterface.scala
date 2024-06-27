import TabelComponent.*

trait iFileIO{
    def load: TableInterface
    def save(gameState: TableInterface): Unit
}