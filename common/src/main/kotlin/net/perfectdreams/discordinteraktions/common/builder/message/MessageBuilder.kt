package net.perfectdreams.discordinteraktions.common.builder.message

import dev.kord.common.annotation.KordPreview
import dev.kord.rest.builder.component.ActionRowBuilder
import dev.kord.rest.builder.component.MessageComponentBuilder
import dev.kord.rest.builder.message.AllowedMentionsBuilder
import dev.kord.rest.builder.message.EmbedBuilder
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Base message builder, used to share common properties and extension methods between create and modify builders.
 */
interface MessageBuilder {
    /**
     * The text content of the message.
     */
    var content: String?

    /**
     * The embedded content of the message.
     */
    var embeds: MutableList<EmbedBuilder>?

    /**
     * The mentions in this message that are allowed to raise a notification.
     * Setting this to null will default to creating notifications for all mentions.
     */
    var allowedMentions: AllowedMentionsBuilder?

    /**
     * The message components to include in this message.
     */
    @OptIn(KordPreview::class)
    var components: MutableList<MessageComponentBuilder>?
}

@OptIn(ExperimentalContracts::class)
inline fun MessageBuilder.embed(block: EmbedBuilder.() -> Unit) {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    embeds = (embeds ?: mutableListOf()).also {
        it.add(EmbedBuilder().apply(block))
    }
}

/**
 * Configures the mentions that should trigger a ping. Not calling this function will result in the default behavior
 * (ping everything), calling this function but not configuring it before the request is build will result in all
 * pings being ignored.
 */
@OptIn(ExperimentalContracts::class)
inline fun MessageBuilder.allowedMentions(block: AllowedMentionsBuilder.() -> Unit = {}) {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    allowedMentions = (allowedMentions ?: AllowedMentionsBuilder()).apply(block)
}


@OptIn(ExperimentalContracts::class)
inline fun MessageBuilder.actionRow(builder: ActionRowBuilder.() -> Unit) {
    contract {
        callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
    }
    components = (components ?: mutableListOf()).also {
        it.add(ActionRowBuilder().apply(builder))
    }
}