package net.perfectdreams.discordinteraktions.common.components

import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.User
import net.perfectdreams.discordinteraktions.common.InteractionContext
import net.perfectdreams.discordinteraktions.common.builder.message.modify.InteractionOrFollowupMessageModifyBuilder
import net.perfectdreams.discordinteraktions.common.requests.RequestBridge
import net.perfectdreams.discordinteraktions.common.entities.messages.EditableMessage
import net.perfectdreams.discordinteraktions.common.entities.messages.Message
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData

open class ComponentContext(
    bridge: RequestBridge,
    sender: User,
    channelId: Snowflake,
    val componentExecutorDeclaration: ComponentExecutorDeclaration,
    val message: Message,
    val dataOrNull: String?,
    interactionData: InteractionData,
    discordInteractionData: DiscordInteraction
) : InteractionContext(bridge, sender, channelId, interactionData, discordInteractionData) {
    val data: String
        get() = dataOrNull ?: error("There isn't any custom data present in this component context!")

    suspend fun deferUpdateMessage() {
        if (!isDeferred) {
            bridge.manager.deferUpdateMessage()
        }
    }

    suspend inline fun updateMessage(block: InteractionOrFollowupMessageModifyBuilder.() -> (Unit))
            = updateMessage(InteractionOrFollowupMessageModifyBuilder().apply(block))

    suspend fun updateMessage(message: InteractionOrFollowupMessageModifyBuilder): EditableMessage {
        // Check if state matches what we expect
        if (message.files?.isNotEmpty() == true && !isDeferred) {
            // If the message has files and our current bridge state is "NOT_REPLIED_YET", then it means that we need to defer before sending the file!
            // (Because currently you can only send files by editing the original interaction message or with a follow up message
            deferUpdateMessage()
        }

        return bridge.manager.updateMessage(message)
    }
}