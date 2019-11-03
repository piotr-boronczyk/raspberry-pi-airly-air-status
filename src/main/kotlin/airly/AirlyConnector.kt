package airly

import mu.KotlinLogging
import us.ihmc.airly.AirlyClientBuilder

class AirlyConnector(apiKey: String, val installationId: Int) {

    val airly = AirlyClientBuilder(apiKey).build()
    private val logger = KotlinLogging.logger { }

    fun getPollutionPercentage(pollutionType: String): Int {

        logger.debug { "started getting data from airly" }
        var measurement: Double = 0.toDouble()
        airly.getMeasurements(installationId).subscribe {
            measurement = it.current.standards.single { it.name == "WHO" && it.pollutant == "PM25" }.percent
        }
        logger.debug { "current air quality percentage is ${measurement.toInt()}" }

        return measurement.toInt()
    }
}