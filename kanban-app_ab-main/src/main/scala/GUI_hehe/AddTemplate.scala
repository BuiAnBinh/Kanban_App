package GUI_hehe

import scalafx.Includes.*
import scalafx.geometry.Insets
import scalafx.scene.control.*
import scalafx.scene.layout.*
import scalafx.application.Platform
import scalafx.scene.control.ButtonBar.ButtonData

/** when the edit category button is clicked, this window pop up.
 * user can change the category name with this */

class AddTemplate extends TextInputDialog:
  title = "Just me making more templates with you hehe"
  headerText = "You can add more template here bestie :D"

  val yeshButtonType = new ButtonType ("Yesh", ButtonData.OKDone)
  this.dialogPane().buttonTypes = Seq(yeshButtonType, ButtonType.Cancel)

  val newTemplate = new TextField():
    promptText = "Input new template here pretty pls"
    
  val yeshButton = this.dialogPane().lookupButton(yeshButtonType)
  yeshButton.disable = true
  
  val grid = new GridPane():
    hgap = 10
    vgap = 10
    padding = Insets(20, 100, 10, 20)
    add(new Label("Template"), 0, 0)
    add(newTemplate, 1, 0)
    
  newTemplate.text.onChange{ (_,_,newstuff) =>
    yeshButton.disable = newstuff.trim().isEmpty}
  
  this.dialogPane().content = grid
  
  Platform.runLater(newTemplate.requestFocus())
