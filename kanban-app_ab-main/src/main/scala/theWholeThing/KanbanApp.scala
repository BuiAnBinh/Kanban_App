package theWholeThing

import scala.collection.mutable.*
import java.time.LocalDate
import scala.util.{Failure, Success, Try}
import java.io.{BufferedReader, File, FileNotFoundException, FileReader, FileWriter}
import scala.io.Source
import io.circe.generic.auto.*
import io.circe.syntax.*
import io.circe.parser.*
import io.circe.*

import java.io._


class KanbanApp:

  val boards = Buffer[Board]()

  def addBoard(board: Board) = boards += board


  def searchCardByTag(tagName: String): Vector[(Board, Buffer[Card])] =
    require (boards.nonEmpty, println("boards are required to exist"))
    val board = boards
    val result = board.map(b => (b, b.card)).filter( b => b._2.contains(tagName)).map( b => (b._1, b._2(tagName)))

    result.toVector
  

  def removeboard(board: Board) =
    require(this.boards.contains(board))
    boards -= board

  var backgroundImage = ""

  val templates = Buffer[String]()

  def addTemplate(newTemplate: String) =
    templates += newTemplate


  case class Data(val boards: Buffer[BoardData], val background: String, val templates: Buffer[String])
  case class CardData(val name: String, deadline: LocalDate, val tags: Buffer[String], val text: String, val catName: String, val attachments: Buffer[String], val checklist: Buffer[String], val doneTask: Buffer[String])
  case class BoardData(val name: String, val cat: Buffer[CategoryData])
  case class CategoryData(val name: String, val cards: Buffer[CardData], val archivedCards: Buffer[CardData], val position: Int)


  def saveData: Unit =
    val boardData = Buffer[BoardData]()
    for i <- boards do
      val cats = Buffer[CategoryData]()
      for l <- i.categories do
        val archivedCards = Buffer[CardData]()
        val cards = Buffer[CardData]()
        for k <- l.card do
          val oneCard = CardData(k.name, k.deadline, k.tagName, k.text, l.name, k.attachments, k.checkList, k.doneTask)
            cards += oneCard
        for m <- l.archivedCard do
          val oneCard = CardData(m.name, m.deadline, m.tagName, m.text, l.name, m.attachments, m.checkList, m.doneTask)
            archivedCards += oneCard
        val oneCat = CategoryData(l.name, cards, archivedCards, l.position)
        cats += oneCat
      val oneBoard = BoardData(i.name, cats)
      boardData += oneBoard
    val kanban = Data(boardData, this.backgroundImage, this.templates)
    val fileWriter = FileWriter(File("./test.json"))
    fileWriter.write(kanban.asJson.toString)
    fileWriter.close()


  def loadData : Unit =

    val file = Source.fromFile("./test.json")

    try

      var contents = ""
      val lines = file.getLines()
      for line <- lines do
        contents += line
      val json : Json = parse(contents).getOrElse(null)
      val emptyData = Data(Buffer[BoardData](), "", Buffer[String]())
      val extractedData = json.as[Data].getOrElse(emptyData)
      println(extractedData)

      backgroundImage = extractedData.background
      for v <- extractedData.templates do
        this.addTemplate(v)

      for i <- extractedData.boards do
        val boardName = i.name
        val categories = i.cat
        val oneBoard = Board(boardName)

        if categories.size < 7 then

          for k <- categories do
            val catName = k.name
            val cards = k.cards
            val archivedCards = k.archivedCards
            val position = k.position
            val oneCategory = Category(catName, oneBoard)
            oneCategory.position = position
            if oneCategory.position >=0 && oneCategory.position <= 5 then
              for l <- cards do
                val cardName = l.name
                val cardsCatName = l.catName
                val tags = l.tags
                val deadline = l.deadline
                val text = l.text
                val attachments = l.attachments
                val checklist = l.checklist
                val doneTask = l.doneTask
                val oneCard = Card(cardName, deadline, cardsCatName)
                for t <- tags do
                if t == "starred" then
                  oneCard.addTag(Starred)
                else if t == "archive" then
                  oneCard.addTag(Archive)
                else
                  oneCard.addTag( Tag(t) )
                oneCard.changeText(text)
                for m <- attachments do
                  oneCard.addAttachement(m)
                for n <- checklist do
                  oneCard.addTask(n)
                for b <- doneTask do
                  oneCard.doTask(b)

                oneCategory.addCard(oneCard)

              for l <- archivedCards do
                val cardName = l.name
                val cardsCatName = l.catName
                val tags = l.tags
                val deadline = l.deadline
                val text = l.text
                val attachments = l.attachments
                val checklist = l.checklist
                val doneTask = l.doneTask
                val oneCard = Card(cardName, deadline, cardsCatName)
                for t <- tags do
                if t == "starred" then
                  oneCard.addTag(Starred)
                else if t == "archive" then
                  oneCard.addTag(Archive)
                else
                  oneCard.addTag( Tag(t) )
                oneCard.changeText(text)
                for m <- attachments do
                  oneCard.addAttachement(m)
                for n <- checklist do
                  oneCard.addTask(n)
                for b <- doneTask do
                  oneCard.doTask(b)

                oneCategory.archiveCard(oneCard)

              oneBoard.addCategory(oneCategory)
            else
              throw StreamCorruptedException("category positions are illegal")
        else
          throw StreamCorruptedException("too many categories")

        this.addBoard(oneBoard)

    catch
      case sth: StreamCorruptedException => println("corrupted file, sorry, can't load board")
      case another: IOException => println("there is Io Exception")
      case other: FileNotFoundException => println("soz there is no such file")
      case _: Throwable => println("idk but some throwable is here to ruin it all")
    finally
      file.close()













