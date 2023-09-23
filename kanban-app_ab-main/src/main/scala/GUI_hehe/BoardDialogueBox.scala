package GUI_hehe

import scalafx.Includes.*
import scalafx.application.Platform
import scalafx.geometry.Insets
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.*
import scalafx.scene.layout.*


class BoardDialogueBox extends TextInputDialog:
/** When the add new board button is pressed, basically, this is the dialogue box that pop up which
 * the user can choose to proceed with creating a new board or cancel */
  title = "New Shiny Board"
  headerText = "Wanna make a new board? hehe"

  val yeshButtonType = new ButtonType ("Yesh", ButtonData.OKDone)
  this.dialogPane().buttonTypes = Seq(yeshButtonType, ButtonType.Cancel)

  val newBoardName = new TextField():
    promptText = "Name the new board pretty pls"

  val grid = new GridPane():
    hgap = 10
    vgap = 10
    padding = Insets(20, 100, 10, 20)
    add(new Label("New board name pretty pls:"), 0, 0)
    add(newBoardName, 1, 0)

  val yeshButton = this.dialogPane().lookupButton(yeshButtonType)
  yeshButton.disable = true

  newBoardName.text.onChange{ (_,_,newName) =>
    yeshButton.disable = newName.trim().isEmpty}

  this.dialogPane().content = grid

  Platform.runLater(newBoardName.requestFocus())
