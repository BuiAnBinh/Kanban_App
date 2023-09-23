package GUI_hehe

import scala.collection.mutable.Buffer

import theWholeThing.Category
import theWholeThing.Card

import scalafx.Includes.*
import scalafx.scene.Scene
import scalafx.scene.layout.*
import scalafx.scene.control.*
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.paint.Color.*
import scalafx.scene.shape.*
import scalafx.scene.text.{Font, FontWeight}

import scalafx.scene.SnapshotParameters
import scalafx.scene.input.*


class CategoryGUI (val cat: Category, var slot: SlotOnBoardUI) extends BorderPane:

  var cardList = cat.card

  this.background = new Background(Array(new BackgroundFill((SkyBlue), CornerRadii.Empty, Insets.Empty)))
  this.border = new Border(new BorderStroke(FloralWhite, BorderStrokeStyle.Solid, CornerRadii.Empty, BorderWidths.Default))

  val deleteCategory = new Button("Delete")
  deleteCategory.style = "-fx-background-color: rgba(197, 239, 228, 0.81); -fx-border-color: Black;"
  deleteCategory.font = Font.font("Verdana", FontWeight.Medium, 12)
  deleteCategory.style = "-fx-background-color: Orangered;"
  deleteCategory.textFill = PapayaWhip

  val addButton = new Button("+ Add Card")
  addButton.style = "-fx-background-color: rgba(197, 239, 228, 0.81); -fx-border-color: Black;"
  addButton.font = Font.font("Verdana", FontWeight.Medium, 12)

  val catButtons = new HBox(3.0, addButton, deleteCategory):
    alignment = Pos.Center

  this.bottom = catButtons

  deleteCategory.onAction = (event) => slot.deleteCat(this)

  this.padding = Insets(5.0)

  /** template part is missing*/
  def newCard() =
    val cardDialog = new CardDialogBox()
    cardDialog.checkStuff()
    cardDialog.show()
    cardDialog.yeshButton match
      case bt: javafx.scene.control.Button => bt.onAction = (event) => {
        val cardName = cardDialog.newCardName.getText
        val deadline = cardDialog.deadline
        val cardText = cardDialog.newCardText
        val newCard = Card(cardName, deadline, cat.name)
        newCard.changeText(cardText)
        cat.addCard(newCard)
        val cardBox = new CardGUI(newCard, this)
        centerPart.children.add(cardBox)
        cardUIList += cardBox
        cardBox.checkState()
      }

  addButton.onAction = (event) => newCard()

  val catTitleLabel = new Label(cat.name)
  catTitleLabel.font = Font.font("Verdana", FontWeight.Bold, 16)
  catTitleLabel.border = new Border(new BorderStroke(FloralWhite, BorderStrokeStyle.Solid, CornerRadii.Empty, BorderWidths.Default))

  this.top = catTitleLabel
  
  val centerPart = new VBox()
  
  this.center = centerPart

  val archivedCardUIList = Buffer[CardGUI]()

  for i <- cat.archivedCard do
    val cardBox = new CardGUI(i, this)
    archivedCardUIList += cardBox
    cardBox.checkState()

  def deleteCard(cardGUI: CardGUI) =
    cat.removeCard(cardGUI.card)
    if !archivedCardUIList.contains(cardGUI) then
      centerPart.children.remove(cardGUI)
      cardUIList -= cardGUI

  val cardUIList = Buffer[CardGUI]()

  for i <- cat.card do
    val cardBox = new CardGUI(i, this)
    if !cat.archivedCard.contains(i) then
      centerPart.children.add(cardBox)
      cardUIList += cardBox
      cardBox.checkState()


/** this method is for the drag and dropping of the card */
  def addCard(cardGUI: CardGUI) =
    cat.addCard(cardGUI.card)
    cardGUI.card.changeCat(this.cat.name)
    cardGUI.changeCat(this)
    centerPart.children.add(cardGUI)
    cardUIList += cardGUI

  def changeToNewCat(newCatUI: CategoryGUI, cardUI: CardGUI): Unit =
    val oldCatUI = cardUI.cat
    if !newCatUI.cardUIList.contains(cardUI) then
      newCatUI.addCard(cardUI)
      cardUI.changeCat(newCatUI)
      oldCatUI.deleteCard(cardUI)


/** stuffs below are for drag and drop of the category */
  def changeSlot(newSlot: SlotOnBoardUI) =
    slot = newSlot

  def dragDetectedCat(event: MouseEvent) =
    val dragBoard = startDragAndDrop(TransferMode.Any:_*)
    val clipBoard = new ClipboardContent()
    clipBoard.putString(this.cat.name)
    dragBoard.setContent(clipBoard)
    this.setScaleX(0.8)
    this.setScaleY(0.8)
    val snapShotPara = new SnapshotParameters()
    dragBoard.setDragView(this.snapshot(snapShotPara, null), event.getX(), event.getY())
    event.consume()

  this.onDragDetected = (event:MouseEvent) => dragDetectedCat(event)

  def dragDoneCat(event: DragEvent) =
    this.setScaleX(1.0)
    this.setScaleY(1.0)

  this.onDragDone = (event) => dragDoneCat(event)


/** stuffs below are for archiving cards */


  def archiveCard(cardUI: CardGUI) =
    cat.archiveCard(cardUI.card)
    archivedCardUIList += cardUI
    cardUIList -= cardUI
    centerPart.children.remove(cardUI)
  
  def unArchiveCard(cardUI: CardGUI) =
    cat.unArchiveCard(cardUI.card)
    archivedCardUIList -= cardUI
    cardUIList += cardUI
    centerPart.children.add(cardUI)
