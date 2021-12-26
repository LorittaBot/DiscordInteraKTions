package net.perfectdreams.discordinteraktions.common.builder.message.create

import net.perfectdreams.discordinteraktions.common.builder.message.MessageBuilder

/**
 * The base builder for creating a new message.
 */
// From Kord
sealed interface MessageCreateBuilder : MessageBuilder {
    /**
     * Whether this message should be played as a text-to-speech message.
     */
    var tts: Boolean?
}
