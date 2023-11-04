package game.util.player

import game.input.Input
import game.model.Game
import game.util.Moving
import game.util.celestialEntities.Light
import gameutil.GameEntity

import scala.collection.mutable.ListBuffer

// Define the Player class that inherits from GameEntity
class Player() extends GameEntity(550, 350, "AmongUsRed.png") {
  var level: Int = 0
  var hp: Int = 100
  var collectedLights: Int = 0
}

// A Player object that extends the Moving trait
object Player extends Moving {
  var movingSpeed = 10.0 // Set moving speed to 10.0

  // Factory method to create a new Player instance
  def apply(): Player = new Player()

  // Method to collect lights and update player's collected lights count
  def collectLights(game: Game): ListBuffer[Light] = {
    val temp: ListBuffer[Light] = new ListBuffer[Light]
    for (light <- game.lightGroup) {
      // Check for contact between light with the player
      if (Math.abs(game.player.posX - light.posX) < 50 && Math.abs(game.player.posY - light.posY) < 50) {
        game.player.collectedLights = game.player.collectedLights + 1
        temp += game.lightGroup.remove(game.lightGroup.indexOf(light))
      }
    }
    temp
  }

  // Method to move the player based on input
  def move(player: Player): Unit = {
    if (Input.wPressed) {
      if (player.posY > 0) player.imageView.y = (player.posY - Player.movingSpeed)
    }
    if (Input.aPressed) {
      if (player.posX > 0) {
        player.imageView.x = (player.posX - Player.movingSpeed)
        player.imageView.scaleX = -1 // Flip game character horizontally to face left
      }
    }
    if (Input.sPressed) {
      if (player.posY < 700) player.imageView.y = (player.posY + Player.movingSpeed)
    }
    if (Input.dPressed) {
      if (player.posX < 1100) {
        player.imageView.x = (player.posX + Player.movingSpeed)
        player.imageView.scaleX = 1 // Flip game character back to face right
      }
    }
  }
}


