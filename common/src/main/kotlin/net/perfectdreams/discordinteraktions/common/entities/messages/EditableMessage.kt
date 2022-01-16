package net.perfectdreams.discordinteraktions.common.entities.messages

import net.perfectdreams.discordinteraktions.common.builder.message.modify.InteractionOrFollowupMessageModifyBuilder

interface EditableMessage {
    suspend fun editMessage(message: InteractionOrFollowupMessageModifyBuilder): EditableMessage
}

// This isn't in the interface because we want this to be inline, allowing users to use suspendable functions within the builder
suspend inline fun EditableMessage.editMessage(block: InteractionOrFollowupMessageModifyBuilder.() -> (Unit))
        = editMessage(InteractionOrFollowupMessageModifyBuilder().apply(block))