package net.perfectdreams.discordinteraktions.context

import dev.kord.common.entity.Option
import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.entities.CommandInteraction
import net.perfectdreams.discordinteraktions.internal.entities.KordUser
import net.perfectdreams.discordinteraktions.api.entities.Message
import net.perfectdreams.discordinteraktions.commands.SlashCommandExecutor
import net.perfectdreams.discordinteraktions.utils.InteractionMessage
import net.perfectdreams.discordinteraktions.utils.MessageBuilder
import net.perfectdreams.discordinteraktions.utils.buildMessage

open class SlashCommandContext(
    val request: CommandInteraction,
    val relativeOptions: List<Option>?,
    internal var bridge: RequestBridge
) {
    val isDeferred
        get() = bridge.state.value != InteractionRequestState.NOT_REPLIED_YET

    val user: User = KordUser(request.member.value?.user?.value ?: request.user.value ?: throw IllegalArgumentException("There isn't a user object present! Discord Bug?"))

    suspend fun defer() {
        if (!isDeferred)
            bridge.manager.defer()
    }

    suspend fun sendMessage(block: MessageBuilder.() -> (Unit)): Message {
        val message = buildMessage(block)
        return sendMessage(message)
    }

    suspend fun sendMessage(message: InteractionMessage): Message {
        if (message.files?.isNotEmpty() == true && !isDeferred) {
            // If the message has files and our current bridge state is "NOT_REPLIED_YET", then it means that we need to defer before sending the file!
            // (Because currently you can only send files by editing the original interaction message or with a follow up message
            defer()
        }

        return bridge.manager.sendMessage(message)
    }
}