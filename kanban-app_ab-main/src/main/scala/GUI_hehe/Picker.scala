package GUI_hehe

import theWholeThing.Card
import theWholeThing.TimeTracker
import scalafx.Includes.*
import scalafx.application.Platform
import scalafx.scene.control.{DatePicker, *}
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.layout.*

import java.time.{LocalDate, LocalDateTime}

/** this is used to pick a deadline. tbh idk if this shit works */
class Picker extends TextInputDialog:
  title = "Not So Cheerful Deadline"
  headerText = "Bestie pick a deadline pls ;3"

  val yeshButtonType = new ButtonType ("Yesh", ButtonData.OKDone)
  this.dialogPane().buttonTypes = Seq(yeshButtonType, ButtonType.Cancel)

  val yeshButton = this.dialogPane().lookupButton(yeshButtonType)
  yeshButton.disable = true

  val tilePane = new TilePane()

  val datePicker = new DatePicker()

  val label = new Label("Choose a date bestie")
  var deadlineOkay = new Label("idk you haven't chosen a date")

  var deadline = LocalDate.now()

  def deadlineNoti() =
    label.text = s"Your chosen deadline is: $deadline"
    val deadlineTracker = new TimeTracker(deadline)
    val remaining = deadlineTracker.dayToDeadline
    if remaining < 0 then
      deadlineOkay.text = "OMG the deadline has passed gg bestie"
    else if remaining == 0 then
      deadlineOkay.text = ":D the deadline is today silly"
    else
      deadlineOkay.text = s"You can do this bestie. $remaining days left"


  datePicker.onAction = (event) => {
    deadline = datePicker.getValue
    deadlineNoti()
    yeshButton.disable = false
  }

  datePicker.showWeekNumbers = true
  tilePane.children = Seq(datePicker, label, deadlineOkay)

  this.dialogPane().content = tilePane

  Platform.runLater(datePicker.requestFocus())
