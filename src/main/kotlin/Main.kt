import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) = runBlocking {

    val actions = Actions(args)

    actions.resetGpio()


        while (true) {
            actions.checkPollution()
        }

}

