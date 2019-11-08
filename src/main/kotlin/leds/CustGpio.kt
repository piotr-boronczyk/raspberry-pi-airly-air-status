package leds

import PollutionType
import com.pi4j.io.gpio.GpioFactory
import com.pi4j.io.gpio.GpioPinDigitalOutput
import com.pi4j.io.gpio.RaspiPin
import mu.KotlinLogging

object CustGpio {

    private val logger = KotlinLogging.logger { }

    private val gpio = GpioFactory.getInstance()

    var animationActive = false

    val dataLeds: List<GpioPinDigitalOutput> = listOf(
        gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00),
        gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01),
        gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02),
        gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03),
        gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04),
        gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05),
        gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06)
    )

    val pollutionTypeLeds: List<Pair<PollutionType, GpioPinDigitalOutput>> = listOf(
        PollutionType.PM25 to gpio.provisionDigitalOutputPin(RaspiPin.GPIO_08),
        PollutionType.PM10 to gpio.provisionDigitalOutputPin(RaspiPin.GPIO_09)
    )

    /*
    *
    * Provide this function with number of how many leds should be on 0-no leds 7- all leds
    *
     */

    fun setDefaultLeds(status: Boolean) {
        dataLeds.map {
            when {
                status -> it.high()
                else -> it.low()
            }
        }
    }

    fun setLedStatus(level: Int) {
        logger.debug("Changing leds level to $level")
        if (level > 7 || level < 0) {
            logger.error("You have provided wrong led level -  $level")
            throw IllegalArgumentException("You have provided the app with wrong number of LEDs - $level")
        }
        dataLeds.forEachIndexed { index, led ->
            if (index < level) {
                logger.debug { "setting ${led.pin} to state HIGH" }
                led.high()
            } else {
                logger.debug { "setting ${led.pin} to state LOW" }
                led.low()
            }
        }
    }

    fun setPollutionTypeLeds(pollutionType: PollutionType) {
        pollutionTypeLeds.forEach {
            it.second.low()
        }
        when (pollutionType) {
            PollutionType.PM25 -> pollutionTypeLeds.single { it.first == pollutionType }.second.high()
            PollutionType.PM10 -> pollutionTypeLeds.single { it.first == pollutionType }.second.high()
        }
    }

    suspend fun startLoadingAnimation(): Boolean {
        logger.debug { "starting animation" }
        animationActive = true
        dataLeds.forEach {
            it.low()
        }
        dataLeds.forEach {
            it.high()
            Thread.sleep(70)
        }
        dataLeds.forEach {
            it.low()
            Thread.sleep(70)
        }
        return if (animationActive) {
            startLoadingAnimation()
        } else {
            animationActive = false
            return false
        }
    }

    fun stopLoadingAnimation() {
        animationActive = false
    }


    fun getCurrentStatus(): Int {
        return dataLeds.filter { it.isHigh }.size
    }


    val settingsButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_07)

}