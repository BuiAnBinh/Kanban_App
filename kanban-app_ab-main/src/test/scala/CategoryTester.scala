import theWholeThing._
import java.time.LocalDate
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CategoryTester extends AnyFlatSpec with Matchers:
  "Category" should "have the correct card when the card is added" in {

    val category = Category("category", Board("board"))
    val card = Card("card1", LocalDate.of(2023,6,6), category.name)
    assume (category.card.isEmpty)
    category.addCard(card)

    assert(category.card.last === card)
  }

  "Category" should "be empty when the card is removed" in {
    val category = Category("category", Board("board"))
    val card = Card("card1", LocalDate.of(2023,6,6), category.name)
    assume (category.card.isEmpty)
    category.addCard(card)

    category.removeCard(card)
    assert(category.card.isEmpty)
  }

  "Category" should "have the new name when name is changed" in {
    val category = Category("category", Board("board"))
    category.changeName("hehe")

    assert(category.name === "hehe")
  }




