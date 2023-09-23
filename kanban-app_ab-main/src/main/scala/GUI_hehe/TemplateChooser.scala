package GUI_hehe

import scalafx.Includes.*
import scalafx.geometry.Insets
import scalafx.scene.control.*
import scalafx.scene.layout.*
import scalafx.application.Platform
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.ComboBox

class TemplateChooser extends TextInputDialog:
  title = "Templates"
  headerText = "You can choose a card template here bestie :D"

  val yeshButtonType = new ButtonType ("Yesh", ButtonData.OKDone)
  this.dialogPane().buttonTypes = Seq(yeshButtonType, ButtonType.Cancel)

  val yeshButton = this.dialogPane().lookupButton(yeshButtonType)
  yeshButton.disable = true

  val kanban = KanbanMain.kanban


  val chosenTemplate = new ComboBox[String]( (kanban.templates :+ "").toSeq ):
    promptText = "Choose template here pretty pls"

  val grid = new GridPane():
    hgap = 10
    vgap = 10
    padding = Insets(20, 100, 10, 20)
    add(new Label("Template"), 0, 0)
    add(chosenTemplate, 1, 0)

  var selected = ""

  chosenTemplate.onAction = (event) => {
    selected = chosenTemplate.getSelectionModel.getSelectedItem
    println("selected is: " + selected)
    yeshButton.disable = false
  }


  this.dialogPane().content = grid


