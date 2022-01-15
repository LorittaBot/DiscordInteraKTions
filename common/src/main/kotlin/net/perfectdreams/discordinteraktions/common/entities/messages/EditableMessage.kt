package net.perfectdreams.discordinteraktions.common.entities.messages

import dev.kord.rest.builder.message.modify.FollowupMessageModifyBuilder
import dev.kord.rest.builder.message.modify.InteractionResponseModifyBuilder
import net.perfectdreams.discordinteraktions.common.builder.message.modify.InteractionOrFollowupMessageModifyBuilder
import net.perfectdreams.discordinteraktions.platforms.kord.utils.runIfNotMissing

interface EditableMessage {
    suspend fun editMessage(block: InteractionOrFollowupMessageModifyBuilder.() -> (Unit)): EditableMessage
    suspend fun editMessage(message: InteractionOrFollowupMessageModifyBuilder): EditableMessage
}