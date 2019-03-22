package tutorial.apisteps

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._
import tutorial.apisteps1.OfferDto

class ApiScenario extends Simulation {

  //configuration
  val httpConf = http
    .baseUrl("https://api.crypto-development.tk")
    .contentTypeHeader("application/json")
    .acceptHeader("application/json")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

  //steps
  object AuthenticationSteps {

    val fetchMe = exec(http("Fetch me data")
      .get("/api/current-user/me")
      .check(status.is(200), bodyString.saveAs("BODY")))


    val logInPawelNowak = exec(http("Login Pawel Nowak")
      .post("/api/login")
      .body(StringBody(
        """
           {
              "login":"paweł nowak",
              "password":"admin"
           }
        """
      )).asJson
      .check(status.is(200)))

    val logInAdmin = exec(http("Login Admin")
      .post("/api/login")
      .body(StringBody(
        """
           {
              "login":"admin",
              "password":"admin"
           }
        """
      )).asJson
      .check(status.is(200)))
  }

  object MarketSteps {
    val fetchMarket = exec(http("fetch markets")
      .get("/api/markets")
      .check(status.is(200), jsonPath("$[0].uuid").find.saveAs("MARKET_UUID")))
  }

  object OfferSteps {

    val createOfferByMarket = exec(http("create offer by market")
      .post("/api/offers")
      .body(StringBody(session => {
        val marketUuid = session("MARKET_UUID").as[String]
        OfferDto.random(marketUuid).toString
      }))
      .check(status.is(201)))


  }

  object UtilSteps {
    def printElement(element: String) = exec(session => {
      val body = session(element).as[String]
      println(body)
      session
    })
  }


  //scenarios
  var basicUserScenario = scenario("Basic user scenario")
    .exec(
      AuthenticationSteps.fetchMe,
      AuthenticationSteps.logInPawelNowak,
      MarketSteps.fetchMarket,
    )

  var adminScenario = scenario("Admin scenario")
    .exec(
      AuthenticationSteps.logInAdmin,
      AuthenticationSteps.fetchMe,
      UtilSteps.printElement("BODY"),
      MarketSteps.fetchMarket,
      UtilSteps.printElement("MARKET_UUID"),

    )

  //run
  setUp(
//        basicUserScenario.inject(atOnceUsers(10)),
    adminScenario.inject(constantUsersPerSec(5) during 1)

  )
    .protocols(httpConf)

}
