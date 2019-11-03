import airly.AirlyConnector
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import leds.CustGpio
import mu.KotlinLogging

class Actions(args: Array<String>) {

    val gpio = CustGpio
    val air = AirlyConnector(args[0], args[1].toInt())

    fun resetGpio() {
        gpio.setDefaultLeds(false)
    }

    suspend fun checkPollution() {
        val animation = GlobalScope.async { CustGpio.startLoadingAnimation() }

        val pollution = air.getPollutionPercentage("PM25")

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

        delay(60000)
    }

    /*button.addListener(GpioPinListenerDigital{
    event: GpioPinDigitalStateChangeEvent? ->
     if (event?.state == PinState.HIGH){
         if ((gpio.getCurrentStatus() + 1 ) > 7) {
             gpio.setLedStatus(0)
         } else {
             gpio.setLedStatus(gpio.getCurrentStatus() + 1)
         }
     }
})*/

    val logger = KotlinLogging.logger {}

}