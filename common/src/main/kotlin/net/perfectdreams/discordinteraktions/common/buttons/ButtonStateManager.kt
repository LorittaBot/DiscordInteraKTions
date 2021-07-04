package net.perfectdreams.discordinteraktions.common.buttons

import net.perfectdreams.discordinteraktions.common.utils.ButtonClickExecutor
import java.util.*

abstract class ButtonStateManager {
    val buttonExecutors = mutableListOf<ButtonClickExecutor<*>>()

    fun registerButtonExecutor(event: ButtonClickExecutor<*>) {
        buttonExecutors.add(event)
    }

    abstract suspend fun storeState(executorSignature: Any, data: String): UUID
    abstract suspend fun getStateById(uniqueId: UUID): ButtonState
    abstract suspend fun getOrNullStateById(uniqueId: UUID): ButtonState?
    abstract suspend fun destroyState(uniqueId: UUID)
}