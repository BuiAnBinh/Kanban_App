package theWholeThing

import scala.collection.mutable._

class Board (var name: String):
  def changeName(newName: String) = name = newName

  val categories = Buffer[Category]()

  def addCategory(cat: Category) =
    categories += cat

  def removeCategory(cat: Category) =
    categories -= cat

  def card: Map[String, Buffer[Card]] =
    var ans = Map[String, Buffer[Card]]()
    val cards = Buffer[Card]()
    for i <- categories do
      cards ++= i.card
    val tags = Buffer[String]()
    for j <- cards do
      tags ++= j.tagName

    for k <- tags do
      val kcardList = Buffer[Card]()
      for h <- cards do
        if h.tagName.contains(k) then
          kcardList += h
      val toAdd = k -> kcardList
      ans += toAdd
    ans

  
  def archivedCard =
    val ans = Buffer[Card]()
    for i <- categories do
      if i.archivedCard.nonEmpty then
        ans ++= i.archivedCard
    ans