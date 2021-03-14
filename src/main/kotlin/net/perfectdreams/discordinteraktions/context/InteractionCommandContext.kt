package net.perfectdreams.discordinteraktions.context

import mu.KotlinLogging
import net.perfectdreams.discordinteraktions.entities.MessageToBeSent
import net.perfectdreams.discordinteraktions.entities.requests.ApplicationCommandRequest

class InteractionCommandContext(
    val request: ApplicationCommandRequest,
    var manager: RequestManager
) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    val user = request.user ?: request.member?.user

    suspend fun defer() {
        manager.defer()
    }

    suspend fun sendMessage(message: String) {
        manager.sendMessage(message)
    }

    suspend fun sendMessage(message: MessageToBeSent) {
        manager.sendMessage(message)
    }
}