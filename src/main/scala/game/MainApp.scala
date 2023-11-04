package game

import game.input.Input
import game.model.Game
import game.util.celestialEntities.{Boss, Health, Meteorite}
import game.util.player.Player
import javafx.{scene => jfxs}
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.Includes._
import scalafx.animation.AnimationTimer
import scalafx.scene.input.{KeyCode, KeyEvent}
import scalafx.scene.layout.AnchorPane
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.scene.{Group, Scene}
import scalafxml.core.{FXMLLoader, NoDependencyResolver}

object MainApp extends JFXApp{

  // Transform path of RootLayout.fxml to URI for resource location
  val rootResource = getClass.getResource("view/RootLayout.fxml")
  // Initialize the FXMLLoader object
  val loader = new FXMLLoader(rootResource, NoDependencyResolver)
  // Load root layout from FXML file
  loader.load();
  // Retrieve the root component BorderPane from the FXML
  val roots = loader.getRoot[jfxs.layout.BorderPane]

  // Initialize stage.
  stage = new PrimaryStage {
    title = "Galaxy Savior"
    resizable = false
    scene = new Scene {
      // Apply DarkTheme stylesheet
      stylesheets += getClass.getResource("view/DarkTheme.css").toString
      root = roots

      // Set up key press event handling
      onKeyPressed = (e: KeyEvent) => {
        if (e.code == KeyCode.W) Input.wPressed = true
        if (e.code == KeyCode.A) Input.aPressed = true
        if (e.code == KeyCode.S) Input.sPressed = true
        if (e.code == KeyCode.D) Input.dPressed = true
        if (e.code == KeyCode.Q) Input.qPressed = true
        if (e.code == KeyCode.Space) {
          Input.spacePressed = !Input.spacePressed
        }
      }

      // Set up key release event handling
      onKeyReleased = (e: KeyEvent) => {
        if (e.code == KeyCode.W) Input.wPressed = false
        if (e.code == KeyCode.A) Input.aPressed = false
        if (e.code == KeyCode.S) Input.sPressed = false
        if (e.code == KeyCode.D) Input.dPressed = false
        if (e.code == KeyCode.Q) Input.qPressed = false
      }
    }
  }

  // Show the Main Menu
  def showMainMenu(): Unit = {
    // Load MainMenu.fxml
    val resource = getClass.getResource("view/MainMenu.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    // Get the root AnchorPane from the loaded FXML
    val mainMenuRoot = loader.getRoot[jfxs.layout.AnchorPane]
    // Set the root AnchorPane as the center content of the BorderPane
    this.roots.setCenter(mainMenuRoot)
  }

  // Show the Instruction
  def showInstruction(): Unit = {
    // Load Instruction.fxml
    val resource = getClass.getResource("view/Instruction.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    // Get the root AnchorPane from the loaded FXML
    val instructionRoot = loader.getRoot[jfxs.layout.AnchorPane]
    // Set the root AnchorPane as the center content of the BorderPane
    this.roots.setCenter(instructionRoot)
  }

  // Show the Game
  def showGame(): Unit = {
    // Create a new AnchorPane to hold the game content
    this.roots.center = {
      new AnchorPane() {
        // Initialize children of the AnchorPane
        children = new Group() {}

        // Create a new instance of the Game class
        var game = new Game()

        // Add a black background rectangle to cover the entire game area
        children.add(Rectangle(1200, 800, Color.Black))

        // Add various game UI elements to the children
        children.add(game.gameText.levelText)
        children.add(game.gameText.hpText)
        children.add(game.gameText.collectedLightsText)
        children.add(game.player.imageView)

        // Initialize timer variables
        var lastTimer = 0L
        val timer: AnimationTimer = AnimationTimer(t => {
          // Calculate the time delta between frames
          val delta = (t - lastTimer) / 1e9

          // Move the player using the Player.move method
          Player.move(game.player)

          // Check if the game should be paused
          if (game.pause) {
            timer.stop // Stop the game timer
            children.add(game.gameText.pauseText) // Display pause text

            // Create an AnimationTimer to continuously check for unpause
            val pause: AnimationTimer = AnimationTimer(t => {
              // If the game is resumed (not paused anymore)
              if (!game.pause) {
                children.remove(game.gameText.pauseText) // Remove pause text
                timer.start // Restart the game timer
              }
            })
            pause.start // Start the pause AnimationTimer
          }

          // Attempt to generate lights
          if (game.lightGroup.isEmpty) {
            // Increment the player's level by 1 as a reward for clearing all the lights
            game.player.level += 1
            // Generate up to 30 new lights for each new level
            for (_ <- 1 to 30) {
              game.generateLights() match {
                case Some(x) => children.add(x.imageView) // If a light is generated, add its image view to the children
                case None => // If no light is generated, do nothing
              }
            }
          }

          // Attempt to spawn obstacles
          game.spawnObstacles(delta) match {
            case Some(x) => children.add(x.imageView) // If an obstacle is spawned, add its image view to the children
            case None => // If no obstacle is spawned, do nothing
          }

          // Attempt to generate health
          game.generateHealth() match {
            case Some(x) => children.add(x.imageView) // If health is generated, add its image view to the children
            case None => // If no health is generated, do nothing
          }

          // Collect lights and move game entities
          for (light <- Player.collectLights(game)) children.remove(light.imageView)
          for (meteorite <- Meteorite.move(game)) children.remove(meteorite.imageView)
          for (boss <- Boss.move(game)) children.remove(boss.imageView)
          for (health <- Health.move(game)) children.remove(health.imageView)

          // Check player status
          if (game.checkPlayerStatus) {
            timer.stop
            children.add(game.gameText.gameOverText)
            children.add(game.gameText.scoreText)
            children.add(game.gameText.quitText)

            // Create an AnimationTimer to continuously check for return to main menu
            val gameOver: AnimationTimer = AnimationTimer(t => {
              if (Input.qPressed) {
                children.remove(game.gameText.gameOverText)
                children.remove(game.gameText.scoreText)
                children.remove(game.gameText.quitText)
                showMainMenu() // Return to the main menu
              }
            })
            gameOver.start
          }

          // Update game text information
          game.updateText

          // Update last timer
          lastTimer = t
        })
        timer.start // Start the game timer
      }
    }
  }

  // Show the Main Menu of the game at the beginning as landing page
  showMainMenu()

}
