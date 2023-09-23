package GUI_hehe

import scalafx.Includes.*
import scalafx.application.Platform
import scalafx.geometry.Insets
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.*
import scalafx.scene.layout.*

class CatDialogBox extends TextInputDialog:
  /** thing that pop up when the "add category" button is pressed. it create category when the category name
   * is input */
  title = "New Shiny Category"
  headerText = "Wanna make a new category? hehe"

  val yeshButtonType = new ButtonType ("Yesh", ButtonData.OKDone)
  this.dialogPane().buttonTypes = Seq(yeshButtonType, ButtonType.Cancel)

  val newCatName = new TextField():
    promptText = "Name the new category pretty pls"

  val grid = new GridPane():
    hgap = 10
    vgap = 10
    padding = Insets(20, 100, 10, 20)
    add(new Label("New category name pretty pls:"), 0, 0)
    add(newCatName, 1, 0)

  val yeshButton = this.dialogPane().lookupButton(yeshButtonType)
  yeshButton.disable = true

  newCatName.text.onChange{ (_,_,newName) =>
    yeshButton.disable = newName.trim().isEmpty}

  this.dialogPane().content = grid

  Platform.runLater(newCatName.requestFocus())


