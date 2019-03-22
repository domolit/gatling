package tutorial

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class FrontendScenario extends Simulation {

  //configuration
  val httpConf = http
    .baseUrl("https://allegro.pl")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")


  //steps
  object BrowseSteps {
    val mainPageStep = exec(http("Go to main page")
      .get("/")
      .check(status.is(200)))


//     val goToLoginPageStep = exec(http("Go to login page")
//      .get("/auth/login")
//      .check(status.is(200)))

    val goToElektronics = exec(http("Go to electronic")
      .get("/dzial/elektronika")
      .check(status.is(
        session =>
          200)))

    def goToTvAndVideo(page:Int) = exec(http("Go to TV and Video")
      .get("/kategoria/tv-i-video-717?p"+page)
      .check(status.is(200)))
  }

  object LoginStep {
    val logInPawelNowak = exec(http("Login Pawel Nowak")
      .post("https://api.crypto-development.tk/api/login")
      .body(StringBody(
        """
           {
              "login":"paweł nowak",
              "password":"admin"
           }
        """
      )).asJson
      .check(status.is(200)))
  }

  //scenarios
  val basicScenario = scenario("Check frontend")
    .exec(
      BrowseSteps.mainPageStep,
      BrowseSteps.goToElektronics,
      BrowseSteps.goToTvAndVideo(1),
      BrowseSteps.goToTvAndVideo(2),
      BrowseSteps.goToTvAndVideo(3),
      BrowseSteps.goToTvAndVideo(4),
    )


  //run
  setUp(
//    basicScenario.inject(
//      atOnceUsers(1)
//    )
      basicScenario.inject(
        rampUsers(10) during 4
    )
      .protocols(httpConf)
  )
}
