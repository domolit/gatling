package computerdatabase

import io.gatling.core.Predef._
import io.gatling.http.Predef._

//noinspection LanguageFeature
class BasicSimulation extends Simulation {

  var OFFER_GENERATOR = new OfferGenerator

  val PROTOCOLS = http
    .baseUrl("http://crypto-real-dev-1281324951.eu-west-1.elb.amazonaws.com") // Here is the root for all relative URLs
    .acceptHeader("application/json") // Here are the common headers
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  //// STEPS
  def checkSessionIsOff() =
    http("check is logged out")
      .get("/api/current-user/me")
      .check(status.is(401))

  def login() =
    http("login")
      .post("/api/login")
      .body(StringBody(
        """
          {
            "login": "Paweł Nowak",
            "password":"admin"
          }
        """
      )).asJson
      .check(status.is(200))

  def checkMe() =
    http("check me")
      .get("/api/current-user/me")
      .check(status.is(200))

  def fetchMarkets() =
    http("fetch markets")
      .get("/api/markets")
      .check(
        jsonPath("$[0].uuid").find.saveAs("MARKET_UUID"), status.in(200)
      )

  def createRandomOffer() =
    http("CREATE OFFER")
      .post("/api/offers")
      .body(StringBody(OFFER_GENERATOR.generateAny().toString)).asJson
      .check(status.in(200))

  def createBuyOffer() =
    http("CREATE BUY OFFER")
      .post("/api/offers")
      .body(StringBody(OFFER_GENERATOR.generateBuy().toString)).asJson
      .check(status.in(200))

  def createSellOffer() =
    http("CREATE SELL OFFER")
      .post("/api/offers")
      .body(
        StringBody(OFFER_GENERATOR.generateSell().toString)).asJson
      .check(status.in(200))

  //// old
//  val healthcheckScenario = scenario("HEALTHCHECK SCENARIO")
//    .exec(http("HEALTHCHECK")
//      .get("/actuator/health")
//      .check(status.is(200)))
//    .exec { session => println(session); session }
//
//  val authenticateScenario = scenario("AUTHENTICATE SCENARIO")
//    .exec(http("LOGIN")
//      .post("/api/login")
//      .body(StringBody("""{ "login": "Paweł Nowak", "password":"admin" }""")).asJson
//      .check(status.is(200))
//      .check(headerRegex("Set-Cookie", "SESSION=(.*)")
//        .saveAs("authToken")))
//    .exec(session => {
//      val response = session("authToken").as[String]
//      println(s"COOKIE : \n$response")
//      cookie = response
//      session
//    })

//  val createOfferScenario = scenario("JUST CREATE OFFER SCENARIO")

    //    .addCookie(Cookie("SESSION", this.cookie))
//    .exec(
//    http("CREATE OFFER")
//      .post("/api/offers")
//      .body(StringBody(OFFER_GENERATOR.generateAny().toString)).asJson
//      .check(status.in(200, 201, 202)))
//
//  val fullLenghtScenario = scenario("FULL SCENARIO")
//    //LOGIN
//    .exec(http("LOGIN")
//    .post("/api/login")
//    .body(StringBody("""{ "login": "Paweł Nowak", "password":"admin" }""")).asJson
//    .check(status.is(200))
//    .check(headerRegex("Set-Cookie", "SESSION=(.*)")
//      .saveAs("authToken")))
//    //ME
//    .exec(http("ME ")
//    .get("/api/current-user/me")
//    .check(bodyString.saveAs("BODY")))
//    .exec(session => {
//      val response = session("authToken").as[String]
//      println(s"COOKIE : \n$response")
//      cookie = response
//      session
//    })
//    //CREATE OFFER
//    .exec(http("CREATE OFFER")
//    .post("/api/offers")
//    .body(StringBody(OFFER_GENERATOR.generateAny().toString)).asJson
//    .check(status.in(200, 201, 202)))
//    .exec(session => printBody(session))


  // RUN

  def longScenario() = scenario("LONG SCENARIO")
    .exec(checkSessionIsOff())
    .exec(login())
    .exec(checkMe())
    .exec(fetchMarkets())
    .exec(session => saveMarketUuid(session))
    .exec(createRandomOffer())

  def scenarios() = List(
    longScenario().inject(rampUsers(1).during(1))
  )


  setUp(scenarios(),
  ).protocols(PROTOCOLS)

  def saveMarketUuid(session: Session) = {
    val response = session("MARKET_UUID").as[String]
    println("Market uuid "+response)
    OFFER_GENERATOR.marketUuid = response

    session
  }


  def printBody(session: Session) = {
    val response = session("MARKET_UUID").as[String]
    println(s"Response body: \n$response")
    session
  }

  def printResponse(session: Session) = {
    println(s"Response body: \n$session")
    session
  }

}
