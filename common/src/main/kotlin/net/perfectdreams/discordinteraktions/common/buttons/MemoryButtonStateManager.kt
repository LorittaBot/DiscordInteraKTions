package net.perfectdreams.discordinteraktions.common.buttons

import java.util.*

class MemoryButtonStateManager : ButtonStateManager() {
    private val storedStates = mutableMapOf<UUID, Pair<Any, Any>>()

    override suspend fun storeState(uniqueId: UUID, executorSignature: Any, data: Any) {
        storedStates[uniqueId] = Pair(executorSignature, data)
    }
    override suspend fun getStateById(uniqueId: UUID): Pair<Any, Any> = storedStates[uniqueId] ?: error("Unknown State!")
    override suspend fun getOrNullStateById(uniqueId: UUID) = storedStates[uniqueId]
    override suspend fun destroyState(uniqueId: UUID) {
        storedStates.remove(uniqueId)
    }
}