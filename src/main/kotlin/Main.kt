import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking

@ExperimentalCoroutinesApi
fun main(args: Array<String>) = runBlocking {

    val actions = Actions(args)

    actions.resetGpio()

    while (true) {
        actions.checkPollution()
    }

}

