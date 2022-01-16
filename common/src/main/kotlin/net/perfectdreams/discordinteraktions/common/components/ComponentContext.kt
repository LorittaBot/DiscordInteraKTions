package net.perfectdreams.discordinteraktions.common.components

import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.Snowflake
import net.perfectdreams.discordinteraktions.common.entities.User
import net.perfectdreams.discordinteraktions.common.InteractionContext
import net.perfectdreams.discordinteraktions.common.builder.message.modify.InteractionOrFollowupMessageModifyBuilder
import net.perfectdreams.discordinteraktions.common.requests.RequestBridge
import net.perfectdreams.discordinteraktions.common.entities.messages.EditableMessage
import net.perfectdreams.discordinteraktions.common.entities.messages.Message
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData
import net.perfectdreams.discordinteraktions.common.utils.suspendableApply

open class ComponentContext(
    bridge: RequestBridge,
    sender: User,
    channelId: Snowflake,
    val message: Message,
    data: InteractionData,
    discordInteractionData: DiscordInteraction
) : InteractionContext(bridge, sender, channelId, data, discordInteractionData) {
    suspend fun deferUpdateMessage() {
        if (!isDeferred) {
            bridge.manager.deferUpdateMessage()
        }
    }

    suspend fun updateMessage(block: suspend InteractionOrFollowupMessageModifyBuilder.() -> (Unit))
            = updateMessage(InteractionOrFollowupMessageModifyBuilder().suspendableApply(block))

    private suspend fun updateMessage(message: InteractionOrFollowupMessageModifyBuilder): EditableMessage {
        // Check if state matches what we expect
        if (message.files?.isNotEmpty() == true && !isDeferred) {
            // If the message has files and our current bridge state is "NOT_REPLIED_YET", then it means that we need to defer before sending the file!
            // (Because currently you can only send files by editing the original interaction message or with a follow up message
            deferUpdateMessage()
        }

        return bridge.manager.updateMessage(message)
    }
}