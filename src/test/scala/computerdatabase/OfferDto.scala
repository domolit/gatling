package computerdatabase

import com.google.gson.Gson

import scala.util.Random

object GsonContainer {
  val GSON = new Gson()
}

//{"pricePerUnit":20,"initialAmount":10,"marketUuid":"1abefb5a-27dd-4754-bfb1-bc5f4c068e32","bidType":"BUY"}
class OfferDto(val pricePerUnit: Int, val initialAmount: Int, val marketUuid: String, var bidType: String) {

  override def toString: String = GsonContainer.GSON.toJson(this)
}

class OfferGenerator() {
  val RANDOM = new Random()
  val types = List("BUY", "SELL")
  var marketUuid = ""

  def generateBuy(): OfferDto = {
    val offer = generateAny()
    offer.bidType = "BUY"
    offer
  }

  def generateSell(): OfferDto = {
    val offer = generateAny()
    offer.bidType = "SELL"
    offer
  }

  def generateAny(): OfferDto = {

    var price = RANDOM.nextInt(1000) + 1000
    var initialAmount = RANDOM.nextInt(1000) + 10
    var offerType = types(RANDOM.nextInt(2))

    var offer = new OfferDto(price, initialAmount, marketUuid, offerType)
    println(offer.toString)

    return offer
  }
}

//check
object RunnerOther {
  def main(args: Array[String]): Unit = {
    val offer = new OfferGenerator

    println(offer.generateAny())
  }
}