package net.perfectdreams.discordinteraktions.common.buttons

import net.perfectdreams.discordinteraktions.common.utils.ButtonClickExecutor
import java.util.*

abstract class ButtonStateManager {
    val buttonExecutors = mutableListOf<ButtonClickExecutor<*>>()

    fun registerButtonExecutor(event: ButtonClickExecutor<*>) {
        buttonExecutors.add(event)
    }

    abstract suspend fun storeState(uniqueId: UUID, executorSignature: Any, data: Any)
    abstract suspend fun getStateById(uniqueId: UUID): Pair<Any, Any>
    abstract suspend fun getOrNullStateById(uniqueId: UUID): Pair<Any, Any>?
    abstract suspend fun destroyState(uniqueId: UUID)
}