package game.model

import game.input.Input
import game.util.celestialEntities.{Boss, Health, Light, Meteorite}
import game.util.player.Player
import gameutil.GameEntity

import scala.collection.mutable.ListBuffer

class Game {
  // Initialize the player object
  var player = Player()

  // Initialize the game text using the player object
  var gameText: GameText = GameText(player)

  // Set the initial spawn delay
  private var spawnDelay: Double = 10

  // Store the time of the last health generation
  private var lastHealthGenerationTime: Long = 0

  // Base interval for health generation in milliseconds
  private val baseHealthGenerationInterval: Long = 20000

  // Initialize a list buffer to hold light entities
  var lightGroup = new ListBuffer[Light]

  // Initialize a list buffer to hold meteorite entities
  var meteoriteGroup = new ListBuffer[Meteorite]

  // Initialize a list buffer to hold boss entities
  var bossGroup = new ListBuffer[Boss]

  // Initialize a list buffer to hold health entities
  var healthGroup = new ListBuffer[Health]

  // Update the game text based on the player's current state
  def updateText {
    gameText.updateText(player)
  }

  // Check if the game should be paused
  def pause: Boolean = {
    // Check if the space key is pressed
    if (Input.spacePressed) {
      return true // Pause the game
    } else {
      return false // Continue the game
    }
  }

  // Generate a light entity and add it to the light group
  def generateLights(): Option[GameEntity] = {
    // Create a new light entity
    var temp = Light()

    // Add the generated light entity to the light group
    lightGroup += temp

    // Return the generated light entity as an Option
    Option(temp)
  }

  // Spawn obstacles based on various conditions
  def spawnObstacles(delta: Double): Option[GameEntity] = {
    // Check if there's a spawn delay
    if (spawnDelay > 0) {
      // Decrease the spawn delay by the delta time
      spawnDelay -= delta

      // Return null as no entity is spawned yet
      Option(null)
    }
    else {
      // Check if player's level is a multiple of 5
      if ((player.level % 5) == 0) {
        // Check if there are no bosses in the boss group
        if (bossGroup.size == 0) {
          // Generate a boss entity and add it to the boss group
          var temp = Boss()
          bossGroup += temp

          // Return the generated boss entity
          Option(temp)
        } else {
          // Return null as no entity is spawned
          Option(null)
        }
      }
      else {
        // Determine the spawn delay based on player's level
        spawnDelay = if (player.level < 20) 0.5 - player.level * 0.02 else 0.1

        // Set a maximum number of meteorites
        val maxMeteorites = 20

        // Calculate the maximum meteorites allowed for the current level
        val maxMeteoritesForLevel = Math.min(player.level * 2, maxMeteorites)

        // Check if the meteorite group size is within the limit
        if (meteoriteGroup.size < maxMeteoritesForLevel) {
          // Generate a meteorite entity and add it to the meteorite group
          var temp = Meteorite(player)
          meteoriteGroup += temp

          // Return the generated meteorite entity
          Option(temp)
        } else {
          // Return null as no entity is spawned
          Option(null)
        }
      }
    }
  }

  // Generate health based on adjusted interval
  def generateHealth(): Option[GameEntity] = {
    // Get the current time
    val currentTime = System.currentTimeMillis()

    // Calculate the adjusted interval based on player's level
    val adjustedInterval = calculateAdjustedInterval(player.level)

    // Check if enough time has passed since the last health generation
    if (currentTime - lastHealthGenerationTime > adjustedInterval) {
      // Generate a health entity and add it to the health group
      var temp = Health()
      healthGroup += temp

      // Update the last health generation time
      lastHealthGenerationTime = currentTime

      // Return the generated health entity
      Option(temp)
    }
    else {
      // Return null as no entity is spawned
      Option(null)
    }
  }

  // Calculate the adjusted interval based on player's level
  private def calculateAdjustedInterval(playerLevel: Int): Long = {
    // Define the minimum and maximum interval limits
    val minInterval: Long = 5000
    val maxInterval: Long = baseHealthGenerationInterval

    // Calculate the scaling factor based on player's level
    val scalingFactor = math.max(1.0 - (playerLevel * 0.05), 0.2)

    // Calculate the adjusted interval using the scaling factor
    val adjustedInterval = (baseHealthGenerationInterval * scalingFactor).toLong

    // Ensure the adjusted interval stays within the limits
    math.min(maxInterval, math.max(minInterval, adjustedInterval))
  }

  // Check the status of the player
  def checkPlayerStatus: Boolean = {
    // Check if the player's hit points (hp) are less than or equal to 0
    if (player.hp <= 0) {
      true // Player's status is considered "game over"
    }
    else {
      false // Player's status is not "game over"
    }
  }
}

object Game {
  // Factory method that returns a new instance of the Game class
  def apply(): Game = new Game()
}

