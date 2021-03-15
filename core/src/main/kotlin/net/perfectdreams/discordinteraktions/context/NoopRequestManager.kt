package net.perfectdreams.discordinteraktions.context

import net.perfectdreams.discordinteraktions.api.entities.Message
import net.perfectdreams.discordinteraktions.utils.InteractionMessage

class NoopRequestManager(bridge: RequestBridge) : RequestManager(bridge) {
    override suspend fun defer() {
        TODO("Not yet implemented")
    }

    override suspend fun sendMessage(message: InteractionMessage): Message {
        TODO("Not yet implemented")
    }
}