package net.perfectdreams.discordinteraktions.context

import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.entities.CommandInteraction
import net.perfectdreams.discordinteraktions.internal.entities.KordUser
import net.perfectdreams.discordinteraktions.api.entities.Message
import net.perfectdreams.discordinteraktions.utils.InteractionMessage
import net.perfectdreams.discordinteraktions.utils.MessageBuilder
import net.perfectdreams.discordinteraktions.utils.buildMessage

open class SlashCommandContext(
    val request: CommandInteraction,
    internal var bridge: RequestBridge
) {
    var isDeferred = false
        private set

    val user: User = KordUser(request.member.value?.user?.value ?: request.user.value ?: throw IllegalArgumentException("There isn't a user object present! Discord Bug?"))

    suspend fun defer() {
        bridge.manager.defer()
        isDeferred = true
    }

    suspend fun sendMessage(block: MessageBuilder.() -> (Unit)): Message {
        val message = buildMessage(block)
        return sendMessage(message)
    }

    suspend fun sendMessage(message: InteractionMessage): Message {
        return bridge.manager.sendMessage(message)
    }
}