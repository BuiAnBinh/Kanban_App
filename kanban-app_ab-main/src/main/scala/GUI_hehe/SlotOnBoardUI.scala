package GUI_hehe

import scala.collection.mutable._

import scalafx.Includes.*
import scalafx.scene.Scene
import scalafx.scene.layout.*
import scalafx.scene.control.*
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.paint.Color.*
import scalafx.scene.shape.*
import scalafx.scene.text.{Font, FontWeight}

/** This is just invisible boxes on the board. used to detect position of the categories for drag and
 * drop */
class SlotOnBoardUI(board: BoardGUI) extends Pane:
  val categoryGUI = Buffer[CategoryGUI]()

  this.style = "-fx-background-color: transparent;"
  this.padding = Insets(5.0)

  def addCat(cat: CategoryGUI) =
    categoryGUI += cat
    this.children.add(cat)
    
  def removeCat(cat: CategoryGUI) =
    categoryGUI -= cat
    this.children.remove(cat) 
    
  def deleteCat(cat: CategoryGUI) =
    categoryGUI -= cat
    this.children.remove(cat)
    board.board.removeCategory(cat.cat)
    