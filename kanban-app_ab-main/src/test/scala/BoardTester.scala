import theWholeThing._
import java.time.LocalDate
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scala.collection.mutable.Buffer

class BoardTester extends AnyFlatSpec with Matchers:


  "Board" should "have the right number of categories when categories are added" in {
    val board = Board("board")
    val category1 = Category("category1", board)
    val category2 = Category("category2", board)
    board.addCategory(category1)
    board.addCategory(category2)

    assert(board.categories.size === 2)
  }

  "Board" should "have the right cards for each tag" in {
    val board = Board("board")
    val category1 = Category("category1", board)
    val category2 = Category("category2", board)

    board.addCategory(category1)
    board.addCategory(category2)
    val card1 = Card("card1", LocalDate.of(2023,6,6), category1.name)
    val card2 = Card("card2", LocalDate.of(2023,9,6), category2.name)

    val tag1 = Tag("banana")
    val tag2 = Tag("apple")
    card1.addTag(tag1)
    card1.addTag(tag2)
    card2.addTag(tag1)

    category1.addCard(card1)
    category2.addCard(card2)

    assert(category1.card.last === card1)
    assert(category2.card.last === card2)

    assert(card1.tags.contains(tag1))
    assert(card1.tags.contains(tag2))
    assert(card2.tags.contains(tag1))

    assert(board.categories == Buffer(category1, category2))

    val stuff = board.card
    assert(stuff("banana") === Buffer(card1, card2))
    assert(stuff("apple") === Buffer(card1))
  }
