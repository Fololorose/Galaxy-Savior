package gameutil

import scalafx.scene.image.{Image, ImageView}

// Abstract base class for game entities
abstract class GameEntity(_x: Double, _y: Double, _imagePath: String) {
  // Load the image from the specified path
  private val image = new Image(_imagePath)

  // Create an ImageView with the loaded image
  var imageView = new ImageView(image){
    x = _x
    y =_y
    fitHeight = 100
    fitWidth = 100
  }

  // Returns the x position of the GameEntity
  def posX: Double = imageView.x.toDouble

  // Returns the y position of the GameEntity
  def posY: Double = imageView.y.toDouble
}



