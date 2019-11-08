import airly.AirlyConnector
import com.pi4j.io.gpio.PinState
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent
import com.pi4j.io.gpio.event.GpioPinListenerDigital
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import leds.CustGpio
import mu.KotlinLogging

class Actions(args: Array<String>) {

    val gpio = CustGpio
    val air = AirlyConnector(args[0], args[1].toInt())
    var pollutionType: PollutionType = PollutionType.PM25

    val listener = gpio.settingsButton.addListener(GpioPinListenerDigital
    { event: GpioPinDigitalStateChangeEvent? ->
        if (event!!.state === PinState.HIGH) {
            runBlocking {
                gpio.stopLoadingAnimation()
                pollutionType =
                    if (PollutionType.values().toList().indexOf(pollutionType) < PollutionType.values().size - 1) {
                        PollutionType.values().toList()[PollutionType.values().toList().indexOf(pollutionType) + 1]
                    } else {
                        PollutionType.values().toList()[0]
                    }
                logger.debug { "Zmieniono typ zanieczyszczenia na ${pollutionType.stringValue}" }
                checkPollution()
            }
        }

    })


    fun resetGpio() {
        gpio.setDefaultLeds(false)
    }


    suspend fun checkPollution() {
        gpio.stopLoadingAnimation()
        val animation = GlobalScope.async { CustGpio.startLoadingAnimation() }

        val pollution = air.getPollutionPercentage(pollutionType.stringValue)

        gpio.stopLoadingAnimation()
        animation.await()
        when {
            pollution <= 15 -> gpio.setLedStatus(1)
            pollution <= 30 -> gpio.setLedStatus(2)
            pollution <= 50 -> gpio.setLedStatus(3)
            pollution <= 100 -> gpio.setLedStatus(4)
            pollution <= 150 -> gpio.setLedStatus(5)
            pollution <= 200 -> gpio.setLedStatus(6)
            else -> gpio.setLedStatus(7)
        }
        gpio.setPollutionTypeLeds(pollutionType)

        delay(600000)
        gpio.stopLoadingAnimation()
    }

    val logger = KotlinLogging.logger {}

}