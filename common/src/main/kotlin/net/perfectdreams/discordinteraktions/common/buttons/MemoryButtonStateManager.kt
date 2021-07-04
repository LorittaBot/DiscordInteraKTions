package net.perfectdreams.discordinteraktions.common.buttons

import java.util.*

class MemoryButtonStateManager : ButtonStateManager() {
    private val storedStates = mutableMapOf<UUID, ButtonState>()

    override suspend fun storeState(executorSignature: Any, data: String): UUID {
        val uniqueId = UUID.randomUUID()
        storedStates[uniqueId] = ButtonState(executorSignature, data)
        return uniqueId
    }
    override suspend fun getStateById(uniqueId: UUID): ButtonState = storedStates[uniqueId] ?: error("Unknown State!")
    override suspend fun getOrNullStateById(uniqueId: UUID) = storedStates[uniqueId]
    override suspend fun destroyState(uniqueId: UUID) {
        storedStates.remove(uniqueId)
    }
}