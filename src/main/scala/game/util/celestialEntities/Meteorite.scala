package game.util.celestialEntities

import game.model.Game
import game.util.Moving
import game.util.player.Player
import gameutil.GameEntity

import scala.collection.mutable.ListBuffer
import scala.util.Random

// Define the Meteorite class that inherits from GameEntity
class Meteorite(player: Player) extends GameEntity(
  if (Random.nextBoolean()) {
    if (Random.nextBoolean()) Random.nextDouble() * 100 else 1200.0
  } else {
    if (Random.nextBoolean()) Random.nextDouble() * 800 else 700.0
  },
  if (Random.nextBoolean()) {
    if (Random.nextBoolean()) Random.nextDouble() * 100 else 800.0
  } else {
    if (Random.nextBoolean()) Random.nextDouble() * 1200 else 1100.0
  },
  "Meteorite.png") {

  private var dx = player.posX - imageView.x.value // Calculate the x distance between meteorite and player's initial position
  private var dy = player.posY - imageView.y.value // Calculate the y distance between meteorite and player's initial position
  val angle = Math.atan2(dy, dx) // Calculate the angle between meteorite and player's initial position
}

// Define a companion object for the Meteorite class
object Meteorite extends Moving {

  var movingSpeed = 2.0 // Set moving speed to 2.0

  // Factory method to create a new Meteorite instance
  def apply(player: Player): Meteorite = new Meteorite(player)

  // Method to move meteorites
  def move(game: Game): ListBuffer[Meteorite] = {
    val temp: ListBuffer[Meteorite] = new ListBuffer[Meteorite]
    for (meteorite <- game.meteoriteGroup) {
      // Calculate the new position based on the angle and player's level
      val newX = meteorite.posX + Math.cos(meteorite.angle) * (Meteorite.movingSpeed + game.player.level / 5)
      val newY = meteorite.posY + Math.sin(meteorite.angle) * (Meteorite.movingSpeed + game.player.level / 5)
      // Check if the new position is within screen boundaries
      if ((newX > 0 && newX < 1100) && (newY > 0 && newY < 700)) {
        // Update the meteorite's position
        meteorite.imageView.x = newX
        meteorite.imageView.y = newY
        // Check for collision with the player
        if (Math.abs(game.player.posX - meteorite.posX) < 80 && Math.abs(game.player.posY - meteorite.posY) < 80) {
          game.player.hp -= 2
          temp += game.meteoriteGroup.remove(game.meteoriteGroup.indexOf(meteorite))
        }
      }
      else {
        // Remove meteorite from list if it's outside the screen boundaries
        temp += game.meteoriteGroup.remove(game.meteoriteGroup.indexOf(meteorite))
      }
    }
    temp
  }
}
