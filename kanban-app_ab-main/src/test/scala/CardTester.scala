//package theWholeThing

import theWholeThing._
import java.time.LocalDate
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CardTester extends AnyFlatSpec with Matchers:
  "Card" should "have the correct tag when the tag is added" in {
    val card = Card("card1", LocalDate.of(2023,6,6), "string")
    assume (card.tags.isEmpty)
    val tag1 = Tag("banana")
    val tag2 = Tag("apple")

    card.addTag(tag1)
    assert(tag1 === card.tags.last)

    card.addTag(tag2)
    assert(tag2 === card.tags.last)

    assert(card.tags.contains(tag1))
  }

  "Card" should "have the correct tags when some tag is removed" in {
    val card = Card("card1", LocalDate.of(2023,6,6), "string")
    assume (card.tags.isEmpty)
    val tag1 = Tag("banana")

    card.addTag(tag1)
    assert(tag1 === card.tags.last)

    card.removeTag(tag1)
    assert(card.tags.isEmpty)
  }

  /** this test needs to be updated according to the day youre doing the test cuz it counts the time
   * from NOW until the stipulated deadline
  "Card" should "have the correct number of days to deadline" in {
    val card1 = Card("card1", LocalDate.of(2023,4,13), "string")
    assert(card1.dayToDeadline === 1)

    val card2 = Card("card1", LocalDate.of(2023,4,18), "string")
    assert(card2.dayToDeadline === 6)
  }*/

  "Card" should "have the new deadline when the deadline is changed" in {
    val card = Card("card1", LocalDate.of(2023,3,20), "string")
    card.changeDeadline(LocalDate.of(2023,3,25))

    assert(card.deadline === LocalDate.of(2023,3,25))
  }

  "Card" should "have the new name when name is changed" in {
    val card = Card("card", LocalDate.of(2023,3,20), "string")
    card.changeName("hehe")

    assert(card.name === "hehe")
  }
