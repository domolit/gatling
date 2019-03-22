package tutorial.basic

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.core.scenario.Simulation

class VeryBasicScenario extends Simulation {

  //configuration
  val httpConf = http
    .baseUrl("https://api.crypto-development.tk")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")


  //steps
  val healthcheckCall = exec(http("Healthcheck call ")
    .get("/actuator/health")
    .check(status.is(200)))


  //scenarios
  val basicScenario = scenario("Very basic scenario")
    .exec(healthcheckCall)


  //run
  setUp(
    basicScenario.inject(
      atOnceUsers(10)
    )
      .protocols(httpConf)
  )
}
