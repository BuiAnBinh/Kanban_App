package GUI_hehe

import scalafx.Includes.*
import scalafx.geometry.Insets
import scalafx.scene.control.*
import scalafx.scene.layout.*
import scalafx.application.Platform
import scalafx.scene.control.ButtonBar.ButtonData

import java.time.LocalDate

/** field to create deadline and comboBox to choose template needs to be added tho. fix the grid accordingly too */


class CardDialogBox extends TextInputDialog:
/** thing that pop up when the "add card" button is pressed. it create a new card when suffifcient info is
input (name, deadline, template)*/
  title = "New Shiny Card"
  headerText = "Wanna make a new card? hehe"

  val yeshButtonType = new ButtonType ("Yesh", ButtonData.OKDone)
  this.dialogPane().buttonTypes = Seq(yeshButtonType, ButtonType.Cancel)

  val newCardName = new TextField():
    promptText = "Name the new card pretty pls"

  val yeshButton = this.dialogPane().lookupButton(yeshButtonType)

  var newNameOkay = false

  newCardName.text.onChange{ (_,_,newName) =>
    newNameOkay = newName.trim().nonEmpty
    checkStuff()
    }

  val addDeadlineButton = new Button("Edit deadline"):
    prefHeight = 30

  var deadline = LocalDate.now()
  var newDeadlineOkay = false

  def newDeadline() =
    val datePicker = new Picker()
    datePicker.show()
    datePicker.yeshButton match
      case  bt: javafx.scene.control.Button => bt.onAction = (event) => {
        deadline = datePicker.deadline
        newDeadlineOkay = true
        checkStuff()
      }

  addDeadlineButton.onAction = (event) => {
    newDeadline()
    }

  def checkStuff() =
    if newDeadlineOkay && newNameOkay then
      yeshButton.disable = false
    else
      yeshButton.disable = true

  var newCardText = ""
  
  val chooseTemplateButton = new Button("Want a template?")

  chooseTemplateButton.onAction = (event) => {
    val dialog = new TemplateChooser
    dialog.show()
    dialog.yeshButton match
      case bt: javafx.scene.control.Button => bt.onAction = (event) => {
        newCardText = dialog.selected
      }
  }
  
  
  val grid = new GridPane():
    hgap = 10
    vgap = 10
    padding = Insets(20, 100, 10, 20)
    add(new Label("New card name pretty pls:"), 0, 0)
    add(newCardName, 1, 0)
    add(new Label("Choose a deadline too:"), 0, 1)
    add(addDeadlineButton, 1, 1)
    add(chooseTemplateButton, 0, 2)


  this.dialogPane().content = grid


  
