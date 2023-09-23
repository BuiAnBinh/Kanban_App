import theWholeThing._
import java.time.LocalDate
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scala.collection.mutable.Buffer

class KanbanAppTester extends AnyFlatSpec with Matchers:
  "Application" should "have multiple boards when multiple boards are added and created" in {
    val application = KanbanApp()
    val board1 = Board("board1")
    val board2 = Board("board2")
    application.addBoard(board1)
    application.addBoard(board2)

    assert(application.boards.contains(board1))
    assert(application.boards.contains(board2))
  }

  "Application" should "return the correct boards when some board is removed" in {
    val application = KanbanApp()
    val board1 = Board("board1")
    val board2 = Board("board2")
    application.addBoard(board1)
    application.addBoard(board2)
    application.removeboard(board1)

    assert(application.boards.head === board2)
    assert(!application.boards.contains(board1))
  }


  "Application" should "return the correct vector of cards for the correct tag" in {
    val application = KanbanApp()
    val board1 = Board("board1")
    val board2 = Board("board2")
    application.addBoard(board1)
    application.addBoard(board2)

    val category1 = Category("category1", board1)
    val category2 = Category("category2", board2)
    board1.addCategory(category1)
    board2.addCategory(category2)
    val card1 = Card("card1", LocalDate.of(2023,6,6), "string")
    val card2 = Card("card2", LocalDate.of(2023,9,6), "string")

    val tag1 = Tag("banana")
    val tag2 = Tag("apple")
    card1.addTag(tag1)
    card1.addTag(tag2)
    card2.addTag(tag1)

    category1.addCard(card1)
    category2.addCard(card2)

    assert(application.searchCardByTag("banana") === Vector( (board1, Buffer(card1)), (board2, Buffer(card2)) ) )
    assert(application.searchCardByTag("apple") === Vector( (board1, Buffer(card1)) ) )
  }