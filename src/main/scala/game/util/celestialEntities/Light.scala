package game.util.celestialEntities

import gameutil.GameEntity

import scala.util.Random

// Define the Light class that inherits from GameEntity
class Light() extends GameEntity(Random.nextInt(1100), Random.nextInt(700), "Light.png") {
  // The constructor sets the initial position of the Light object with random coordinates
}

// Define a companion object for the Light class
object Light {
  // Factory method to create a new Light instance
  def apply(): Light = new Light()
}
