package net.perfectdreams.discordinteraktions.common.utils

import net.perfectdreams.discordinteraktions.api.entities.optional.Optional
import net.perfectdreams.discordinteraktions.common.NamedFile

/**
 * Utility container for message modify builder. This class contains
 * all possible fields as optionals.
 */
// From Kord: https://github.com/kordlib/kord/blob/0.8.x/rest/src/main/kotlin/builder/message/modify/MessageModifyStateHolder.kt
internal class MessageModifyStateHolder {
    var files: Optional<MutableList<NamedFile>> = Optional.Missing()

    var content: Optional<String?> = Optional.Missing()

    var embeds: Optional<MutableList<EmbedBuilder>> = Optional.Missing()

    var flags: Optional<MessageFlags?> = Optional.Missing()

    var allowedMentions: Optional<AllowedMentionsBuilder> = Optional.Missing()

    var attachments: Optional<MutableList<DiscordAttachment>> = Optional.Missing()

    var components: Optional<MutableList<MessageComponentBuilder>> = Optional.Missing()
}