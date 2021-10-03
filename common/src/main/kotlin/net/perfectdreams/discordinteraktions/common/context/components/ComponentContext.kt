package net.perfectdreams.discordinteraktions.common.context.components

import dev.kord.common.entity.Snowflake
import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.common.builder.message.modify.EphemeralInteractionMessageModifyBuilder
import net.perfectdreams.discordinteraktions.common.builder.message.modify.PublicInteractionMessageModifyBuilder
import net.perfectdreams.discordinteraktions.common.context.InteractionContext
import net.perfectdreams.discordinteraktions.common.context.RequestBridge
import net.perfectdreams.discordinteraktions.common.entities.messages.EditableEphemeralMessage
import net.perfectdreams.discordinteraktions.common.entities.messages.EditablePersistentMessage
import net.perfectdreams.discordinteraktions.common.entities.messages.Message
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData

open class ComponentContext(
    bridge: RequestBridge,
    sender: User,
    channelId: Snowflake,
    val message: Message,
    data: InteractionData
) : InteractionContext(bridge, sender, channelId, data) {
    suspend fun deferUpdateMessage() {
        if (!isDeferred) {
            bridge.manager.deferUpdateMessage()
        }
    }

    suspend fun updateMessage(block: PublicInteractionMessageModifyBuilder.() -> (Unit))
            = updateMessage(PublicInteractionMessageModifyBuilder().apply(block))

    suspend fun updateEphemeralMessage(block: EphemeralInteractionMessageModifyBuilder.() -> (Unit))
            = updateEphemeralMessage(EphemeralInteractionMessageModifyBuilder().apply(block))

    private suspend fun updateMessage(message: PublicInteractionMessageModifyBuilder): EditablePersistentMessage {
        // Check if state matches what we expect
        if (message.files?.isNotEmpty() == true && !isDeferred) {
            // If the message has files and our current bridge state is "NOT_REPLIED_YET", then it means that we need to defer before sending the file!
            // (Because currently you can only send files by editing the original interaction message or with a follow up message
            deferUpdateMessage()
        }

        return bridge.manager.updateMessage(message)
    }

    private suspend fun updateEphemeralMessage(message: EphemeralInteractionMessageModifyBuilder): EditableEphemeralMessage {
        return bridge.manager.updateEphemeralMessage(message)
    }
}