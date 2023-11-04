package game.util.celestialEntities

import game.model.Game
import game.util.Moving
import gameutil.GameEntity

import scala.collection.mutable.ListBuffer

// Define the Boss class that inherits from GameEntity
class Boss() extends GameEntity(550, 350, "Boss.png") {
  var hp: Int = 100 // Initialize Boss's hit points to 100
}

// Define a companion object for the Boss class
object Boss extends Moving {
  var movingSpeed = 1.0 // Set the default moving speed for Boss objects to 1.0

  // Factory method to create a new Boss instance
  def apply(): Boss = new Boss()

  // Method to move Boss objects
  def move(game: Game): ListBuffer[Boss] = {
    val temp: ListBuffer[Boss] = new ListBuffer[Boss]
    for (boss <- game.bossGroup) {
      // Calculate the new position based on the position of boss between player
      val dx = game.player.posX - boss.posX
      val dy = game.player.posY - boss.posY
      val dist = math.sqrt(dx * dx + dy * dy)
      boss.imageView.x = boss.posX + dx / dist * (Boss.movingSpeed + game.player.level / 5)
      boss.imageView.y = boss.posY + dy / dist * (Boss.movingSpeed + game.player.level / 5)

      // Check for collision with the player
      if (Math.abs(game.player.posX - boss.posX) < 80 && Math.abs(game.player.posY - boss.posY) < 80) {
        game.player.hp -= 1
      }
      else {
        // Remove boss from list if lightGroup is empty
        if (game.lightGroup.isEmpty) {
          temp += game.bossGroup.remove(game.bossGroup.indexOf(boss))
        }
      }
    }
    temp
  }
}
