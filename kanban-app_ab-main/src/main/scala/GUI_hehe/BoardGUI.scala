package GUI_hehe

import scala.collection.mutable._

import theWholeThing.{Board, Category}

import scalafx.Includes.*
import scalafx.scene.Scene
import scalafx.scene.layout.*
import scalafx.scene.control.*
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.paint.Color.*
import scalafx.scene.shape.*
import scalafx.scene.text.{Font, FontWeight}

import scalafx.scene.input.*

/** Basically, this constitutes the center pane of the GUI. it is a border Pane. the bottom is the button 
 * to add category (when the max number of category has not been exceeded) and all and the middle has 
 * Hboxes of categories. when boards are changed only this part is changed. when the board's background is 
 * changed, this one's background is changed */

class BoardGUI (val board: Board) extends BorderPane:

  val root = this
  var catList = board.categories

  val nameLabel = new Label(board.name)
  nameLabel.font = Font.font("Verdana", FontWeight.Bold, 14)

  val topPane = new HBox():
    padding = Insets(5.0)
    alignment = Pos.Center

  topPane.children = nameLabel

  this.top = topPane

  val centerPane = new HBox():
    padding = Insets(5.0)

  this.center = centerPane

  val addCatButton = new Button("Add category"):
    prefHeight = 30
    padding = Insets(5.0)

  val buttonBox = new HBox(addCatButton):
    alignment = Pos.Center
    padding = Insets(5.0)

  this.bottom = buttonBox

  val UIslots =
    val result = Buffer[SlotOnBoardUI]()
    var i = 0
    while (i < 6) do
      val ui = new SlotOnBoardUI(this)
      result += ui
      i += 1
      centerPane.children.add(ui)
    result.toVector

  for k <- board.categories do
    val nextAvailSlot = this.UIslots(k.position)//this.UIslots.find(x => x.categoryGUI.isEmpty).get
    val newCatUI = new CategoryGUI(k, nextAvailSlot)
    nextAvailSlot.addCat(newCatUI)
    newCatUI.onDragOver = (event) => dragOver(event)
    newCatUI.onDragDropped = (event) => {
      if event.getDragboard.hasString then
        val cardName = event.getDragboard.getString
        val movedCard = cardUIs.find( ui => ui.card.name == cardName).get
        changeToNewCat(newCatUI, movedCard)
      event.setDropCompleted(true)
      event.consume()}


  def addCat() =
    if board.categories.size < 6 then
      val catDialogue = new CatDialogBox()
        catDialogue.show()
        catDialogue.yeshButton match
          case bt: javafx.scene.control.Button => bt.onAction = (event) => {
            val catName = catDialogue.newCatName.getText
            val newCat = new Category(catName, board)
            board.addCategory(newCat)
            val availUI = this.UIslots.find(x => x.categoryGUI.isEmpty).get
            val slotPosition = this.UIslots.indexOf(availUI)
            val catUI = new CategoryGUI(newCat, availUI)
            newCat.position = slotPosition
            availUI.addCat(catUI)

            catUI.onDragOver = (event) => dragOver(event)
            catUI.onDragDropped = (event) => {
              if event.getDragboard.hasString then
                val cardName = event.getDragboard.getString
                val movedCard = cardUIs.find( ui => ui.card.name == cardName).get
                changeToNewCat(catUI, movedCard)
              event.setDropCompleted(true)
              event.consume()}
    }


  addCatButton.onAction = (event) => addCat()

    
  def catUIs =
    val ans = Buffer[CategoryGUI]()
    for i <- this.UIslots do
      ans ++= i.categoryGUI
    ans

  for i <- this.UIslots do
    i.onDragOver = (event) => dragOver(event)
    i.onDragDropped = (event) => {
      if event.getDragboard.hasString then
        val catName = event.getDragboard.getString
        val movedCat = catUIs.find( ui => ui.cat.name == catName).get
        changeToNewSlot(i, movedCat)
        movedCat.cat.position = this.UIslots.indexOf(i)
      event.setDropCompleted(true)
      event.consume()}


/** the stuffs below is for drag and drop of the card */

  def changeToNewCat(newCatUI: CategoryGUI, cardUI: CardGUI): Unit =
    val oldCatUI = cardUI.cat
    if newCatUI.cat.name != cardUI.cat.cat.name then
      newCatUI.addCard(cardUI)
      cardUI.changeCat(newCatUI)
      oldCatUI.deleteCard(cardUI)

  def dragOver(event: DragEvent) =
    if event.gestureSource != event.gestureTarget && event.dragboard.hasString then
      event.acceptTransferModes(TransferMode.Move)
    event.consume()

  
  def cardUIs =
    val ans = Buffer[CardGUI]()
    for i <- catUIs do
      ans ++= i.cardUIList
    ans




/** stuffs below are for drag and drop of the categories */
  def changeToNewSlot(newSlot: SlotOnBoardUI, catUI: CategoryGUI): Unit =
    val oldSlot = catUI.slot
    if newSlot.categoryGUI.nonEmpty then
      val originalCat = newSlot.categoryGUI.head
      newSlot.removeCat(originalCat)
      oldSlot.removeCat(catUI)
      oldSlot.addCat(originalCat)
      originalCat.cat.position = this.UIslots.indexOf(oldSlot)
      originalCat.changeSlot(oldSlot)
    newSlot.addCat(catUI)
    catUI.changeSlot(newSlot)
    

  /** stuffs below are for archiving cards */
  def archivedCardUI: Buffer[CardGUI] =
    val ans = Buffer[CardGUI]()
    for i <- catUIs do
      ans ++= i.archivedCardUIList
    ans

