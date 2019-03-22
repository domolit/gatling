package tutorial.apisteps1

import java.util.concurrent.ThreadLocalRandom

import com.google.gson.Gson

class OfferDto(val pricePerUnit: Int, val initialAmount: Int, val marketUuid: String, var bidType: String) {

  override def toString = OfferDto.GSON.toJson(this)
}

object OfferDto {
  val GSON = new Gson()
  val statuses = List("BUY", "SELL")

  def random(marketUuid: String): OfferDto = {

    val offer = new OfferDto(
      ThreadLocalRandom.current.nextInt(1000) + 1,
      ThreadLocalRandom.current.nextInt(1000) + 1,
      marketUuid,
      statuses(ThreadLocalRandom.current.nextInt(2))
    )
    println(offer)
    offer
  }
}
