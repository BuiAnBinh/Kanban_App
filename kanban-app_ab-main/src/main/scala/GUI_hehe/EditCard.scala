package GUI_hehe

import theWholeThing.Card

import scalafx.Includes.*
import scalafx.geometry.Insets
import scalafx.scene.control.*
import scalafx.scene.layout.*
import scalafx.application.Platform
import scalafx.scene.control.ButtonBar.ButtonData

/** this thing pops up when you press the edit button on the card window. In this, you can input stuffs to edit your
 * card name, deadline, tag, add/remove task to check list, add attachment, add text */
class EditCard (val card: Card) extends TextInputDialog(defaultValue = card.text):
  title = "Editing mode baby"
  headerText = "You can edit your card here bestie"

  val yeshButtonType = new ButtonType ("Yesh", ButtonData.OKDone)
  this.dialogPane().buttonTypes = Seq(yeshButtonType, ButtonType.Cancel)
  
  val yeshButton = this.dialogPane().lookupButton(yeshButtonType)

  val newCardName = new TextField():
    promptText = "Wanna rename it?"

  var newNameOkay = false
  
  newCardName.text.onChange{ (_,_,newName) =>
    newNameOkay = newName.trim().nonEmpty}

  val addTag = new TextField():
    promptText = "Wanna add a new tag?"
  
  var newTagOkay = false
  addTag.text.onChange{ (_,_,newTag) =>
    newTagOkay = newTag.trim().nonEmpty
    }

  var newTaskOkay = false
  
  val newTask = new TextField():
    promptText = "Wanna add a new task to the card?"
    
  newTask.text.onChange{ (_,_,newTask) =>
    newTaskOkay = newTask.trim().nonEmpty}
    
  
  val editDeadlineButton = new Button("Edit deadline"):
    prefHeight = 30
  
  var deadlineNew = card.deadline
  
  def newDeadline() =
    val datePicker = new Picker()
    datePicker.show()
    datePicker.yeshButton match
      case  bt: javafx.scene.control.Button => bt.onAction = (event) => {
        deadlineNew = datePicker.deadline
        card.changeDeadline(deadlineNew)}

  editDeadlineButton.onAction = (event) => newDeadline()

  var text = this.defaultValue

  val newTextLabel = new Label("You can edit text for the card here bestie:")
  val newCardText = this.editor


  def checkText(): String =
    text = newCardText.getText
    text

  val layout = new VBox():
    padding = Insets(20)


  layout.children = Seq(newCardName, addTag, newTask, newTextLabel, newCardText, editDeadlineButton)
  
  this.dialogPane().content = layout

