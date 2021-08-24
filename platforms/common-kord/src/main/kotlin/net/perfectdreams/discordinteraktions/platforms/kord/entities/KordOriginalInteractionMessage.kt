package net.perfectdreams.discordinteraktions.platforms.kord.entities

import dev.kord.common.entity.Snowflake
import dev.kord.rest.builder.message.modify.EphemeralInteractionResponseModifyBuilder
import dev.kord.rest.builder.message.modify.PublicInteractionResponseModifyBuilder
import dev.kord.rest.builder.message.modify.embed
import dev.kord.rest.service.RestClient
import net.perfectdreams.discordinteraktions.common.entities.Message
import net.perfectdreams.discordinteraktions.common.utils.InteractionMessage
import net.perfectdreams.discordinteraktions.platforms.kord.utils.toKordAllowedMentions
import net.perfectdreams.discordinteraktions.platforms.kord.utils.toKordEmbedBuilder

class KordOriginalInteractionMessage(
    private val rest: RestClient,
    private val applicationId: Snowflake,
    private val interactionToken: String,
    override val content: String,
    private val isEphemeral: Boolean
) : Message {
    override val id: net.perfectdreams.discordinteraktions.api.entities.Snowflake
        get() = error("Original Interaction Messages do not have an ID!")

    override suspend fun editMessage(message: InteractionMessage): Message {
        val newMessage = rest.interaction.modifyInteractionResponse(
            applicationId,
            interactionToken,
            if (isEphemeral) {
                EphemeralInteractionResponseModifyBuilder().apply {
                    this.content = message.content
                    this.allowedMentions = message.allowedMentions?.toKordAllowedMentions()
                    this.embeds = message.embeds?.let { it.map { it.toKordEmbedBuilder() } }?.toMutableList()
                }.toRequest()
            } else {
                PublicInteractionResponseModifyBuilder().apply {
                    this.content = message.content
                    this.allowedMentions = message.allowedMentions?.toKordAllowedMentions()
                    this.embeds = message.embeds?.let { it.map { it.toKordEmbedBuilder() } }?.toMutableList()

                    val filePairs = message.files?.map { it.key to it.value }
                    if (filePairs != null)
                        files?.addAll(filePairs)
                }.toRequest()
            }
        )

        return KordMessage(newMessage)
    }
}