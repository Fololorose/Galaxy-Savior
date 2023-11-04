package game.view

import game.MainApp
import scalafx.scene.control.{Alert, ButtonType}
import scalafxml.core.macros.sfxml

// Controller class for the main menu view
@sfxml
class MainMenuController {

  // Method to handle the "Start Game" button click
  def handleGame(): Unit = {
    MainApp.showGame() // Call the showGame method from MainApp to start the game
  }

  // Method to handle the "How to Play" button click
  def handleInstruction(): Unit = {
    MainApp.showInstruction() // Call the showInstruction method from MainApp to show the instruction view
  }

  // Method to handle the "Quit Game" button click
  def handleClose(): Unit = {
    // Create an alert dialog to confirm quitting the game
    val alert = new Alert(Alert.AlertType.Warning) {
      initOwner(MainApp.stage)
      title = "Quit Game"
      headerText = "Confirm Quit"
      contentText = "Are you sure you want to quit the game?"
      buttonTypes = Seq(ButtonType.Yes, ButtonType.No)
    }

    // Show the alert and wait for the user's response
    val result = alert.showAndWait()

    // Check the user's response and take appropriate action
    result match {
      case Some(ButtonType.Yes) =>
        // If the user clicks Yes, execute System.exit(0) to quit the game
        System.exit(0)
      case _ =>
      // If the user clicks No or closes the alert without selecting Yes, do nothing (just close the alert)
    }
  }
}

