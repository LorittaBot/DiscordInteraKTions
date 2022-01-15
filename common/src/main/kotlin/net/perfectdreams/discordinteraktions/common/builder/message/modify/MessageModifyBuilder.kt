package net.perfectdreams.discordinteraktions.common.builder.message.modify


import dev.kord.common.entity.DiscordAttachment
import dev.kord.rest.builder.message.modify.FollowupMessageModifyBuilder
import dev.kord.rest.builder.message.modify.InteractionResponseModifyBuilder
import net.perfectdreams.discordinteraktions.common.builder.message.MessageBuilder

sealed interface MessageModifyBuilder : MessageBuilder {
    var attachments: MutableList<DiscordAttachment>?

    fun toFollowupMessageModifyBuilder(): FollowupMessageModifyBuilder
    fun toInteractionMessageResponseModifyBuilder(): InteractionResponseModifyBuilder
}