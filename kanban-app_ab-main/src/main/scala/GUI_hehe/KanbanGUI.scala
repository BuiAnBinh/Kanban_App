package GUI_hehe

import theWholeThing.*

import scala.collection.mutable.Buffer
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.layout.*
import scalafx.scene.control.*
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.image.Image
import scalafx.scene.paint.Color.*
import scalafx.scene.shape.*
import scalafx.scene.text.{Font, FontWeight}
import scalafx.stage.FileChooser


/** The whole GUI is a BorderPane that only has the top, center, and bottom panel. the top has the search bar and all
 * and the center has some HBox structures with each category as a box */
object KanbanMain extends JFXApp3:

  val kanban = KanbanApp()

  kanban.loadData

  def start(): Unit =
    stage = new JFXApp3.PrimaryStage:
      title = "It's Kanban time baby! (respectfully)"
      width = 1280
      height = 800

    val biggestLayoutLevel = new BorderPane()

    if kanban.backgroundImage != "" then
      biggestLayoutLevel.style = s"-fx-background-image: url(${kanban.backgroundImage}); -fx-background-size: cover, auto;"
    else
      biggestLayoutLevel.style = "-fx-background-color: MistyRose;"

    stage.scene = new Scene(biggestLayoutLevel)

    val boardUIList = Buffer[BoardGUI]()



/** Make a search bar */
    val searchField = new TextField:
      prefWidth = 250
      prefHeight = 30
      promptText = "Search for tags here :D"

    val searchButton = new Button("Go!")

    val searchBar = new HBox(20.0, searchField, searchButton):
      alignment = Pos.Center
      padding = Insets(10.0)


/** Make search result display box */
    var keyword = ""

    val hideSearchesButton = new Button("Hide searches"):
      font = Font.font("Verdana", FontWeight.Bold, 12)
      style = "-fx-background-color: rgba(255, 107, 129, 0.8);"

    val leftPane = new BorderPane()
    leftPane.bottom = hideSearchesButton


    val searchResultLabel = new Label():
      font = Font.font("Verdana", FontWeight.Bold, 12)
      textFill = SeaShell

    def searchResultBox = new HBox(searchResultLabel):
      prefWidth = 150
      background = new Background(Array(new BackgroundFill((PowderBlue), CornerRadii.Empty, Insets.Empty)))

/** Make the search function */
    def searchTag(tagName: String) =
      val tag = tagName.toLowerCase.trim
      var text = "Result(s):"
      if kanban.boards.isEmpty then
        searchResultLabel.text = "You don't have any boards :D"
      else
        if kanban.searchCardByTag(tag).isEmpty then
          searchResultLabel.text = "No match"
        else
          searchResultLabel.text = {
            val result = kanban.searchCardByTag(tag)
            for i <- result do
              text ++= s"\n${result.indexOf(i)+1}) Card with card \nname(s): \n  ${i._2.map(card => card.name).mkString(",\n  ")} \nfrom board: \n  ${i._1.name}"
            text
          }
      searchResultBox

    def searchCard() =
      keyword = searchField.text()
      searchTag(keyword)
      leftPane.center = searchResultBox
      biggestLayoutLevel.left = leftPane
      hideSearchesButton.onAction = (event) => biggestLayoutLevel.children.remove(leftPane)

    searchButton.onAction = (event) => searchCard()

/** this stuffs is to accedd multiple board and to delete them as well */
    val boardAccessButton = new Button("Boards"):
      prefHeight = 30

    def boardChooser() =
      val newBoardChooser = new VBox():
        background = new Background(Array(new BackgroundFill((LightPink), CornerRadii.Empty, Insets.Empty)))
        border = new Border(new BorderStroke(LightCoral, BorderStrokeStyle.Solid, CornerRadii.Empty, BorderWidths.Default))

      if kanban.boards.nonEmpty then
        val yesBoardLabel = new Label("Here are your boards bestie:"):
          font = Font.font("Verdana", FontWeight.Bold, 16)
        newBoardChooser.children.add(yesBoardLabel)

        for i <- kanban.boards do
          val boardNameLabel = new Label(i.name):
            font = Font.font("Verdana", FontWeight.Bold, 12)
            padding = Insets(5.0)
          val deleteBoardButton = new Button("Delete"):
            padding = Insets(5.0)
            font = Font.font("Verdana", FontWeight.Medium, 12)
            style = "-fx-background-color: Orangered;"
            textFill = PapayaWhip
          val chooseBoardButton = new Button("Open board"):
            padding = Insets(5.0)
            style = "-fx-background-color: LightCyan;"
            font = Font.font("Verdana", FontWeight.Medium, 12)
            textFill = LightSeaGreen
          val boardView = new HBox(10.0, boardNameLabel, chooseBoardButton, deleteBoardButton):
            padding = Insets(10.0)
            alignment = Pos.Center
          newBoardChooser.children.add(boardView)

          deleteBoardButton.onAction = (event) => {
            kanban.removeboard(i)
            val toBeRemoved = boardUIList.find(k => k.board == i).get
            boardUIList -= toBeRemoved
            newBoardChooser.children.remove(boardView)}

          chooseBoardButton.onAction = (event) => {
            val boardUI = boardUIList.find(k => k.board == i).get
            biggestLayoutLevel.center = boardUI}

      else
        val noBoardsLabel = new Label("You don't have any boards rn. Wanna make one bestie? :D"):
          font = Font.font("Verdana", FontWeight.Bold, 18)
          alignment = Pos.Center
        newBoardChooser.children.add(noBoardsLabel)
      biggestLayoutLevel.center = newBoardChooser

    boardAccessButton.onAction = (event) => boardChooser()



  /** When this button is pressed, basically, there will be some kind of dialogue box (class BoardDialogueBox)
   * that pop up which the user can choose to proceed with creating a new board or cancel */
    val addBoardButton = new Button("+"):
      shape = Circle(30)
      prefWidth = 30

    if kanban.boards.nonEmpty then
      for i <- kanban.boards do
        val boardUI = new BoardGUI(i)
        boardUIList += boardUI


    def makeNewBoard() =
      val boardDialogue = new BoardDialogueBox()
      boardDialogue.show()
      boardDialogue.yeshButton match
        case bt: javafx.scene.control.Button => bt.onAction = (event) => {
          val boardName = boardDialogue.newBoardName.getText
          val newBoard = new Board(boardName)
          kanban.addBoard(newBoard)
          val boardUI = new BoardGUI(newBoard)
          boardUIList += boardUI
          biggestLayoutLevel.center = boardUI
        }

    addBoardButton.onAction = (event) => makeNewBoard()

    /** the stuffs below are to show the archived cards */
    val archiveAccessButton = new Button("Archive"):
      prefHeight = 30


    def showArchived() =
      val newArchivedView = new VBox():
        background = new Background(Array(new BackgroundFill((Pink), CornerRadii.Empty, Insets.Empty)))
        border = new Border(new BorderStroke(Salmon, BorderStrokeStyle.Solid, CornerRadii.Empty, BorderWidths.Default))

      val archivedCards = Buffer[Card]()

      if kanban.boards.nonEmpty then
        for i <- kanban.boards do
          if i.archivedCard.nonEmpty then
            archivedCards ++= i.archivedCard
        if archivedCards.nonEmpty then
          for k <- archivedCards do
            val oriCatName = k.catName
            println("archived card name:" + k.name)
            val catUIs = Buffer[CategoryGUI]()
            if boardUIList.nonEmpty then
              for l <- boardUIList do
                catUIs ++= l.catUIs
            val oriCatUI = catUIs.find( x => x.cat.name == oriCatName).get
            println("original cat name:" + oriCatUI.cat.name)

            val archivedCardUIList = Buffer[CardGUI]()
            for i <- boardUIList do
              archivedCardUIList ++= i.archivedCardUI

            val archiveCardUI = archivedCardUIList.find(x => x.card == k).get
            println(s"archived card GUI's card name : ${archiveCardUI.card.name}")

            //val archiveCardUI = new CardGUI(k, oriCatUI)
            newArchivedView.children.add(archiveCardUI)
            println(newArchivedView.children)
        else
          val noArchiveLabel = new Label("No boards rn duh :D"):
            font = Font.font("Verdana", FontWeight.Bold, 18)
            alignment = Pos.Center
          newArchivedView.children.add(noArchiveLabel)
        biggestLayoutLevel.center = newArchivedView
      else
        val noBoardsLabel = new Label("No boards rn duh :D"):
          font = Font.font("Verdana", FontWeight.Bold, 18)
          alignment = Pos.Center
        newArchivedView.children.add(noBoardsLabel)
      biggestLayoutLevel.center = newArchivedView

    archiveAccessButton.onAction = (event) => showArchived()

    val saveBoardButton = new Button("SAVE ME <3"):
      prefHeight = 30
      style = "-fx-background-color: LightSalmon;"
      font = Font.font("Verdana", FontWeight.Medium, 12)

    saveBoardButton.onAction = (event) => kanban.saveData

    val starredAccessButton = new Button("Starred"):
      prefHeight = 30

    starredAccessButton.onAction = (event) => searchStar()


    def searchStar() =
      searchTag("Starred")
      leftPane.center = searchResultBox
      biggestLayoutLevel.left = leftPane
      hideSearchesButton.onAction = (event) => biggestLayoutLevel.children.remove(leftPane)

/** stuff over here is to change the background of the app */
    val setUpAccessButton = new Button("Change background"):
      prefHeight = 30

    val fileChooser = new FileChooser()

    setUpAccessButton.onAction = (event) => {
      val selectedFile = fileChooser.showOpenDialog(stage)
      if selectedFile != null then
        val fileString = selectedFile.toURI.toString
        val img = new Image(fileString)
        if !img.isError then
          biggestLayoutLevel.style = s"-fx-background-image: url($fileString); -fx-background-size: cover, auto;"
          kanban.backgroundImage = fileString
    }
    val resetBackground = new Button("Reset background"):
      prefHeight = 30

    resetBackground.onAction = (event) => {
      biggestLayoutLevel.style = "-fx-background-color: MistyRose;"
      kanban.backgroundImage = ""
    }

    val leftButtons = new HBox(20.0, saveBoardButton, boardAccessButton, addBoardButton):
      alignment = Pos.Center
      padding = Insets(30.0)

    val rightButtons = new HBox(20.0, archiveAccessButton, starredAccessButton, setUpAccessButton, resetBackground):
      alignment = Pos.Center
      padding = Insets(30.0)

    val topPanel = new HBox(20.0, leftButtons, searchBar, rightButtons):
      alignment = Pos.Center
      padding = Insets(30.0)

    biggestLayoutLevel.top = topPanel

    val addTemplateButton = new Button("Add templates?"):
      alignment = Pos.Center
      padding = Insets(10.0)
      style = "-fx-background-color: rgba(142, 222, 238, 0.8);"
      font = Font.font("Verdana", FontWeight.Medium, 12)

    biggestLayoutLevel.bottom = addTemplateButton

    def addTemplate() =
      val dialog = new AddTemplate()
      dialog.show()
      dialog.yeshButton match
        case bt: javafx.scene.control.Button => bt.onAction = (event) => {
          val newTemplate = dialog.newTemplate.getText
          kanban.addTemplate(newTemplate)
        }

    addTemplateButton.onAction = (event) => addTemplate()
