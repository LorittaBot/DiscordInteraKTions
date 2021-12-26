package net.perfectdreams.discordinteraktions.common.builder.message.modify


import dev.kord.common.entity.DiscordAttachment
import net.perfectdreams.discordinteraktions.common.builder.message.MessageBuilder

sealed interface MessageModifyBuilder : MessageBuilder {
    var attachments: MutableList<DiscordAttachment>?
}