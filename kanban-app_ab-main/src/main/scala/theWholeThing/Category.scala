package theWholeThing

import scala.collection.mutable._

class Category (var name: String, val board: Board):
  val card = Buffer[Card]()
  
  var position = 0
    
  def changeName(newName: String) = 
    name = newName
    
  def removeCard(needToBeRemoved: Card) = 
    require(card.contains(needToBeRemoved))
    card -= needToBeRemoved
    
  def addCard(needToBeAdded: Card) =
    card += needToBeAdded

  val archivedCard = Buffer[Card]()
  
  def archiveCard(theCard: Card) =
    archivedCard += theCard
    card -= theCard
    
  def unArchiveCard(theCard: Card) =
    archivedCard -= theCard
    card += theCard