package game.model

import game.util.player.Player
import scalafx.scene.paint.Color
import scalafx.scene.text.{Font, FontWeight, Text, TextAlignment}

// Class to manage the in-game text display
class GameText(player: Player) {

  // Text to display player's current level
  var levelText = new Text(20, 40, "Level : " + player.level)
  levelText.setFont(Font.font("Segoe UI", 30))
  levelText.setFill(Color.White)

  // Text to display player's current HP
  var hpText: Text = new Text(20, 80, "HP : " + player.hp)
  hpText.setFont(Font.font("Segoe UI", 30))
  hpText.setFill(Color.White)

  // Text to display the number of lights collected by the player
  var collectedLightsText: Text = new Text(20, 120, "Collected Lights : " + player.collectedLights)
  collectedLightsText.setFont(Font.font("Segoe UI", 30))
  collectedLightsText.setFill(Color.White)

  // Text to display "GAME PAUSED" when the game is paused
  val pauseText = new Text("GAME PAUSED") {
    y = 425
    wrappingWidth = 1200
    textAlignment = TextAlignment.Center
  }
  pauseText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 120))
  pauseText.setFill(Color.White)

  // Text to display "GAME OVER" when the game is over
  val gameOverText = new Text("GAME OVER") {
    y = 400
    wrappingWidth = 1200
    textAlignment = TextAlignment.Center
  }
  gameOverText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 150))
  gameOverText.setFill(Color.Red)

  // Text to display total score of player based on collected lights when game is over
  val scoreText = new Text("Score: " + player.collectedLights) {
    y = 500
    wrappingWidth = 1200
    textAlignment = TextAlignment.Center
  }
  scoreText.setFont(Font.font("Segoe UI", 60))
  scoreText.setFill(Color.White)

  // Text to instruct player to press "Q" to go back to the main menu
  val quitText = new Text("Press \"Q\" to Back to Main") {
    y = 600
    wrappingWidth = 1200
    textAlignment = TextAlignment.Center
  }
  quitText.setFont(Font.font("Segoe UI", 40))
  quitText.setFill(Color.White)

  // Method to update the displayed text based on the player's status
  def updateText(player: Player) = {
    levelText.setText("Level : " + player.level)
    hpText.setText("HP : " + player.hp)
    collectedLightsText.setText("Collected Lights : " + player.collectedLights)
    scoreText.setText("Score: " + player.collectedLights)
  }
}

// Companion object for the GameText class
object GameText {
  // Factory method to create a new GameText instance
  def apply(player: Player): GameText = new GameText(player)
}

