package GUI_hehe

import theWholeThing.{Card, Tag, Starred, Archive}

import scala.collection.mutable.Buffer

import scalafx.application.JFXApp3

import scalafx.Includes.*
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.*
import scalafx.scene.layout.*
import scalafx.application.Platform
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.paint.Color.*
import scalafx.scene.shape.*
import scalafx.scene.text.{Font, FontWeight}

import scalafx.scene.SnapshotParameters
import scalafx.scene.input.*

import scalafx.stage.FileChooser
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.text.Text
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, ButtonType}

/** this is how cards are displayed in the board. it is a VBox of progress bar, tags thingy, name and
 * info button(these 2 are tgt). when the info button is clicked, it expands and display other info of the card */


class CardGUI(val card: Card, var cat: CategoryGUI) extends VBox:
  this.border = new Border(new BorderStroke(DarkSalmon, BorderStrokeStyle.Solid, CornerRadii.Empty, BorderWidths.Default))
  this.style = "-fx-background-color: Cornsilk;"

  val openCardButton = new Button("Info")
  openCardButton.style = "-fx-background-color: rgba(175, 239, 228, 0.8);"
  openCardButton.font = Font.font("Verdana", FontWeight.Medium, 8)

  def changeCat(newCat: CategoryGUI) =
    cat = newCat

  def cardNameLabel = new Label(card.name)
  cardNameLabel.font = Font.font("Verdana", FontWeight.Bold, 10)

  def nameAndInfoAndStar = new HBox(15.0, cardNameLabel, openCardButton, starButton):
    alignment = Pos.Center
    padding = Insets(5.0)

/** the stuffs below are mainly what get displayed when the card is opened */
  val cardText = new Label()
  cardText.font = Font.font("Verdana", FontWeight.Medium, 12)
  cardText.text = card.text


  val editCardButton = new Button("Edit"):
    padding = Insets(1.0)
  editCardButton.style = "-fx-background-color: rgba(247, 159, 201, 0.8);"
  editCardButton.font = Font.font("Verdana", FontWeight.Medium, 8)

  def checkListSize = card.checkList.size

  def doneTaskSize = card.doneTask.size

  var checkBoxList = Buffer[CheckBox]()

  def cardEditor() =
    val cardDialog = new EditCard(card)
    cardDialog.show()
    cardDialog.yeshButton match
      case bt: javafx.scene.control.Button => bt.onAction = (event) => {
        if cardDialog.newNameOkay then
          val cardName = cardDialog.newCardName.getText
          card.changeName(cardName)
          cardNameLabel.text = card.name
          nameAndInfoAndStar

        val tagName = cardDialog.addTag.getText
        if cardDialog.newTagOkay then
          val newTag = Tag(tagName)
          card.addTag(newTag)

        val newDeadline = cardDialog.deadlineNew
        card.changeDeadline(newDeadline)
        deadlineLabel

        val text = cardDialog.checkText()
        card.changeText(text)
        cardText.text = card.text

        val newTask = cardDialog.newTask.getText
        if cardDialog.newTaskOkay then
          card.addTask(newTask)
          val checkBox = new CheckBox(newTask)
          cardTasksBox.children.add(checkBox)
          checkBoxList += checkBox
          checkCheckBox(checkBox)

        checkProgress()
        checkState()
      }

    checkState()
  editCardButton.onAction = (event) => cardEditor()

  def checkCheckBox(box: CheckBox)=
    val checked = box.selected.value
    if checked && !card.doneTask.contains(box.text.value) then
      card.doTask(box.text.value)
    if !checked && card.doneTask.contains(box.text.value) then
      card.undoTask(box.text.value)


  def checkProgress() =
    if checkBoxList.nonEmpty then
      for i <- checkBoxList do
        checkCheckBox(i)

  checkProgress()

  val archiveButton = new Button("Archive")
  archiveButton.style = "-fx-background-color: rgba(186, 182, 245, 0.8);"
  archiveButton.font = Font.font("Verdana", FontWeight.Medium, 8)
  archiveButton.padding = Insets(1.0)

  val starredTag = Starred

  var starred = false

  if card.tags.contains(starredTag) then starred = true

  val starButton = new Button()
  starButton.padding = Insets(5.0)
  starButton.setPrefSize(15,8)
  if starred then
    starButton.style = "-fx-shape: \"M23.6,0c-3.4,0-6.3,2.7-7.6,5.6C14.7,2.7,11.8,0,8.4,0C3.8,0,0,3.8,0,8.4c0,9.4,9.5,11.9,16,21.26.1-9.3,16-12.1,16-21.2C32,3.8,28.2,0,23.6,0z\"; -fx-background-color: Salmon;"
  else
    starButton.style = "-fx-shape: \"M23.6,0c-3.4,0-6.3,2.7-7.6,5.6C14.7,2.7,11.8,0,8.4,0C3.8,0,0,3.8,0,8.4c0,9.4,9.5,11.9,16,21.26.1-9.3,16-12.1,16-21.2C32,3.8,28.2,0,23.6,0z\"; -fx-background-color: FloralWhite;"
  starButton.border = new Border(new BorderStroke(Crimson, BorderStrokeStyle.Solid, CornerRadii.Empty, BorderWidths.Default))


  def star() =
    if !starred then
      starButton.style = "-fx-shape: \"M23.6,0c-3.4,0-6.3,2.7-7.6,5.6C14.7,2.7,11.8,0,8.4,0C3.8,0,0,3.8,0,8.4c0,9.4,9.5,11.9,16,21.26.1-9.3,16-12.1,16-21.2C32,3.8,28.2,0,23.6,0z\"; -fx-background-color: Salmon;"
      card.addTag(starredTag)
      card.tagName
      starred = true
    else
      starButton.style = "-fx-shape: \"M23.6,0c-3.4,0-6.3,2.7-7.6,5.6C14.7,2.7,11.8,0,8.4,0C3.8,0,0,3.8,0,8.4c0,9.4,9.5,11.9,16,21.26.1-9.3,16-12.1,16-21.2C32,3.8,28.2,0,23.6,0z\"; -fx-background-color: FloralWhite;"
      card.removeTag(starredTag)
      card.tagName
      starred = false


  starButton.onAction = (event) => star()

  val buttonCluster = new HBox(20.0, starButton, archiveButton):
    padding = Insets(5.0)

  def deadlineLabel = new Label(s"Deadline: ${card.deadline.toString}")
  deadlineLabel.font = Font.font("Verdana", FontWeight.Bold, 12)

  val tagList  = new Label()
  tagList.font = Font.font("Verdana", FontWeight.Bold, 12)

  val cardTasks = new Label()
  cardTasks.font = Font.font("Verdana", FontWeight.Bold, 12)

  val cardTasksBox = new VBox()

  if card.checkList.nonEmpty then
    for i <- card.checkList do
      val checkBox = new CheckBox(i)
      cardTasksBox.children.add(checkBox)
      checkBoxList += checkBox
      if card.doneTask.contains(i) then
        checkBox.selected = true
  checkProgress()

  var progressBarColour = PaleGreen

  var barlength = 197

  if card.dayToDeadline < 7 then
    progressBarColour = Tomato

  def progressDisplay() =
    val pane = new Pane()
    val maxBar = Rectangle(barlength, 20)
    maxBar.fill = Pink // idk if this works
    /** we can only have a progress bard when there is a checklist!!!!! */
    if checkListSize > 0 && doneTaskSize > 0 then
      checkProgress()
      val width = (doneTaskSize.toDouble/checkListSize.toDouble)*barlength
      val progress = Rectangle(width, 20, progressBarColour)
      pane.children = Seq(maxBar, progress)
      maxBar.fill = Pink
    else
      if card.dayToDeadline < 7 then
        maxBar.fill = Tomato
      else
        maxBar.fill = Pink
      pane.children = maxBar
    pane

  val deleteButton = new Button("Delete"):
    style = "-fx-background-color: Orangered;"
  deleteButton.textFill = PapayaWhip

  deleteButton.onAction = (event) => cat.deleteCard(this)

  val attachmentsBox = new BorderPane()

  val attachmentLabel = new Label("Attachments:")

  attachmentsBox.top = attachmentLabel

  val attacheHBox = new HBox():
    padding = Insets(5.0)

  attachmentsBox.center = attacheHBox


  for i <- card.attachments do
    val imageDisplay = new Pane():
      prefHeight = 60
      prefWidth = 80
      padding = Insets(3.0)

    val removeAttachmentButton = new Button():
      style = "-fx-background-color: Transparent;"
      prefHeight = 60
      prefWidth = 80

    val img = new Image(i)
    if img.isError then
      val theText = new Text(i)
      theText.wrappingWidth = 80
      imageDisplay.children.add(theText)
    else
      imageDisplay.style = s"-fx-background-image: url($i); -fx-background-size: cover, auto;"

    imageDisplay.children.add(removeAttachmentButton)

    attacheHBox.children.add(imageDisplay)

    removeAttachmentButton.onAction = (event) => {
      val alert = new Alert(AlertType.Confirmation):
        title = "Confirmation"
        headerText = "Im just double-checking"
        contentText = "Bestie you sure you wanna delete this attachment?"
      val result = alert.showAndWait()
      result match
        case Some(ButtonType.OK) => {
          card.removeAttachement(i)
          attacheHBox.children.remove(imageDisplay)
        }
    }




  val addAttachmentButton = new Button("wanna add an attachment?"):
    prefHeight = 10
    style = "-fx-background-color: rgba(255, 185, 211, 0.8);"
    font = Font.font("Verdana", FontWeight.Medium, 10)
    padding = Insets(1.0)

  val fileChooser = new FileChooser()

  addAttachmentButton.onAction = (event) => {
    val selectedFile = fileChooser.showOpenDialog(new JFXApp3.PrimaryStage)
    if selectedFile != null then
      val fileString = selectedFile.toURI.toString
      card.addAttachement(fileString)
      val imageDisplay = new Pane():
        prefHeight = 60
        prefWidth = 80
        padding = Insets(3.0)

      val removeAttachmentButton = new Button():
        style = "-fx-background-color: Transparent;"
        prefHeight = 60
        prefWidth = 80
      imageDisplay.children.add(removeAttachmentButton)

      val img = new Image(fileString)
      if img.isError then
        val theText = new Text(fileString)
        theText.wrappingWidth = 80
        theText.font = Font.font("Verdana", FontWeight.Light, 9)
        imageDisplay.children.add(theText)
      else
        imageDisplay.style = s"-fx-background-image: url($fileString); -fx-background-size: cover, auto;"

      attacheHBox.children.add(imageDisplay)

      removeAttachmentButton.onAction = (event) => {
        val alert = new Alert(AlertType.Confirmation):
          title = "Confirmation"
          headerText = "Im just double-checking"
          contentText = "Bestie you sure you wanna delete this attachment?"
        val result = alert.showAndWait()
        result match
          case Some(ButtonType.OK) => {
            card.removeAttachement(fileString)
            attacheHBox.children.remove(imageDisplay)
          }
      }
    checkState()}



  var openCard = false

  def checkState() =
    if !openCard then
      this.prefWidth = 200
      this.prefHeight = 62
      checkProgress()
      barlength = 197
      openCardButton.text = "Info"
      this.children = Seq(nameAndInfoAndStar, progressDisplay())
      openCard = true
    else
      openCardButton.text = "Close info"
      this.prefWidth = 500
      this.prefHeight = 400
      checkProgress()
      barlength = 497
      if card.checkList.nonEmpty then
        cardTasks.text = "Checklist"
      if card.tagName.nonEmpty && card.tagName != Buffer("starred") && card.tagName != Buffer("archive") && card.tagName != Buffer("archive", "starred") && card.tagName != Buffer("starred","archive") then
        tagList.text = s"Tags: ${card.tagName.filter(i => i != "starred").filter(i => i != "archive").mkString(", ")}"
      this.children = Seq(nameAndInfoAndStar, editCardButton, buttonCluster, deadlineLabel, tagList, cardTasks, cardTasksBox, cardText, deleteButton, progressDisplay(), addAttachmentButton, attachmentsBox)
      openCard = false



  openCardButton.onAction = (event) => checkState()


  /** the stuffs below is for archiving */
  val archiveTag = Archive
  var archived = false
  
  if card.tags.contains(archiveTag) then
    archived = true
    archiveButton.text = "Un-Archive"
    deleteButton.disable = true
  
  def archiveCard() =
    if !archived then
      archiveButton.text = "Un-Archive"
      card.addTag(archiveTag)
      archived = true
      cat.archiveCard(this)
      deleteButton.disable = true
    else
      archiveButton.text = "Archive"
      card.removeTag(archiveTag)
      archived = false
      cat.unArchiveCard(this)
      deleteButton.disable = false

  archiveButton.onAction = (event) => archiveCard()

  /** the stuffs below is for drag and drop of the card */
  def dragDetected(event: MouseEvent) =
    val dragBoard = startDragAndDrop(TransferMode.Any:_*)
    val clipBoard = new ClipboardContent()
    clipBoard.putString(this.card.name)
    dragBoard.setContent(clipBoard)
    this.setScaleX(0.8)
    this.setScaleY(0.8)
    val snapShotPara = new SnapshotParameters()
    dragBoard.setDragView(this.snapshot(snapShotPara, null), event.getX(), event.getY())
    event.consume()

  this.onDragDetected = (event:MouseEvent) => dragDetected(event)

  def dragDone(event: DragEvent) =
    this.setScaleX(1.0)
    this.setScaleY(1.0)

  this.onDragDone = (event) => dragDone(event)



