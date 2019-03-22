package tutorial

import io.gatling.app.Gatling
import io.gatling.core.config.GatlingPropertiesBuilder

object TutorialRunner {

  def main(args: Array[String]): Unit = {

    val sim = classOf[FrontendScenario].getName
    val props = new GatlingPropertiesBuilder
    props.simulationClass(sim)

    Gatling.fromMap(props.build)
    //    print("hello world")
  }


}
