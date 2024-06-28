package util
import model.TableInterface


trait FileIOInterface{
    def load: TableInterface
    def save(gameState: TableInterface): Unit
}