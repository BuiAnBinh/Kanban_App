package theWholeThing

import java.io._
import java.time.LocalDate
import scala.collection.mutable._
import scala.io.Source

class Card (var name: String, var deadline: LocalDate, var catName: String):

  private var tracker = new TimeTracker(deadline)

  def changeCat(newCatName: String) =
    catName = newCatName
  
  def changeName(newName: String) = 
    name = newName

  def changeDeadline(newDeadline: LocalDate) =
    deadline = newDeadline
    tracker = new TimeTracker(deadline)
  
  var tags = Buffer[Tag]()

  def tagName =
    var result = Buffer[String]()
    for i <- tags do
      result += i.name
    result


  def removeTag(tag:Tag) = 
    require (tags.contains(tag))
    tags -= tag
    tagName
  

  def addTag(tag: Tag) =
    tags += tag
    tagName

  var checkList = Buffer[String]()
  
  var doneTask = Buffer[String]()

  def addTask(task: String) =
    checkList += task
    
  def doTask(task: String) =
    doneTask += task
  
  def undoTask(task: String) =
    doneTask -= task

  def dayToDeadline = tracker.dayToDeadline

  var text = ""

  def changeText(newText:String) =
    text = newText

  val attachments = Buffer[String]()

  def addAttachement(stuff: String) =
    attachments += stuff

  def removeAttachement(stuff: String) =
    attachments -= stuff


