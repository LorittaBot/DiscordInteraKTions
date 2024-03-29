package net.perfectdreams.discordinteraktions.platforms.kord.entities.messages

import dev.kord.common.entity.DiscordMessage
import dev.kord.common.entity.Snowflake
import dev.kord.rest.builder.message.modify.EphemeralInteractionResponseModifyBuilder
import dev.kord.rest.service.RestClient
import net.perfectdreams.discordinteraktions.common.entities.messages.EphemeralMessage
import net.perfectdreams.discordinteraktions.common.utils.EphemeralMessageBuilder
import net.perfectdreams.discordinteraktions.common.utils.buildEphemeralMessage
import net.perfectdreams.discordinteraktions.platforms.kord.utils.toKordActionRowBuilder
import net.perfectdreams.discordinteraktions.platforms.kord.utils.toKordAllowedMentions
import net.perfectdreams.discordinteraktions.platforms.kord.utils.toKordEmbedBuilder

class KordEditedOriginalInteractionEphemeralMessage(
    private val rest: RestClient,
    private val applicationId: Snowflake,
    private val interactionToken: String,
    private val message: DiscordMessage
) : KordEphemeralMessage(message) {
    override suspend fun editMessage(block: EphemeralMessageBuilder.() -> Unit): EphemeralMessage {
        val message = buildEphemeralMessage(block)
        val newMessage = rest.interaction.modifyInteractionResponse(
            applicationId,
            interactionToken,
            EphemeralInteractionResponseModifyBuilder().apply {
                this.content = message.content
                this.allowedMentions = message.allowedMentions?.toKordAllowedMentions()
                this.embeds = message.embeds?.let { it.map { it.toKordEmbedBuilder() } }?.toMutableList()
                this.components = message.components?.map { it.toKordActionRowBuilder() }?.toMutableList()
            }.toRequest()
        )

        return KordEditedOriginalInteractionEphemeralMessage(
            rest,
            applicationId,
            interactionToken,
            newMessage
        )
    }
}