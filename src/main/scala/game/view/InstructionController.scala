package game.view

import game.MainApp
import scalafxml.core.macros.sfxml

// Controller class for the instruction view
@sfxml
class InstructionController {

  // Method to handle the main menu button click
  def handleMainMenu(): Unit = {
    MainApp.showMainMenu() // Call the showMainMenu method from MainApp to navigate back to the main menu
  }
}

