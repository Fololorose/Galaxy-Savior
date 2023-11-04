package game.util.celestialEntities

import game.model.Game
import game.util.Moving
import gameutil.GameEntity

import scala.collection.mutable.ListBuffer

// Define the Health class that inherits from GameEntity
class Health() extends GameEntity(550, 350, "Health.png") {
  imageView.fitWidth = 60 // Set the new width value for the image view
  imageView.fitHeight = 60 // Set the new height value for the image view
  val randomAngle = Math.random() * Math.PI * 2 // Generate a random angle for health's moving direction
}

// Define a companion object for the Health class
object Health extends Moving {
  var movingSpeed = 2.0 // Set the default moving speed for Health objects to 2.0

  // Factory method to create a new Health instance
  def apply(): Health = new Health()

  // Method to move Health objects
  def move(game: Game): ListBuffer[Health] = {
    val temp: ListBuffer[Health] = new ListBuffer[Health]
    for (health <- game.healthGroup) {
      // Calculate the new position based on the random angle of health's moving direction
      val newX = health.posX + Math.cos(health.randomAngle) * Health.movingSpeed
      val newY = health.posY + Math.sin(health.randomAngle) * Meteorite.movingSpeed
      // Check if the new position is within screen boundaries
      if ((newX > 0 && newX < 1100) && (newY > 0 && newY < 700)) {
        // Update the health's position
        health.imageView.x = newX
        health.imageView.y = newY
        // Check for collision with the player
        if (Math.abs(game.player.posX - health.posX) < 50 && Math.abs(game.player.posY - health.posY) < 50) {
          // Increase player's HP, but limit it to a maximum of 100
          game.player.hp = Math.min(game.player.hp + 5, 100)
          temp += game.healthGroup.remove(game.healthGroup.indexOf(health))
        }
      }
      else {
        // Remove health from list if it's outside the screen boundaries
        temp += game.healthGroup.remove(game.healthGroup.indexOf(health))
      }
    }
    temp
  }
}
