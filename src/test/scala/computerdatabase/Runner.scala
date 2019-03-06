package computerdatabase

import io.gatling.app.Gatling
import io.gatling.core.config.GatlingPropertiesBuilder

object Runner {


  def main(args: Array[String]): Unit = {

    val sim = classOf[BasicSimulation].getName
    val props = new GatlingPropertiesBuilder
    props.simulationClass(sim)

    Gatling.fromMap(props.build)

  }

}
