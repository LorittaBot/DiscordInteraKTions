package net.perfectdreams.discordinteraktions.webserver.requests.managers

import dev.kord.common.entity.Choice
import dev.kord.common.entity.DiscordAutoComplete
import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.DiscordModal
import dev.kord.common.entity.InteractionResponseType
import dev.kord.common.entity.MessageFlag
import dev.kord.common.entity.MessageFlags
import dev.kord.common.entity.Snowflake
import dev.kord.common.entity.optional.Optional
import dev.kord.common.entity.optional.coerceToMissing
import dev.kord.common.entity.optional.map
import dev.kord.common.entity.optional.optional
import dev.kord.common.entity.optional.toPrimitive
import dev.kord.rest.builder.interaction.ModalBuilder
import dev.kord.rest.json.request.AutoCompleteResponseCreateRequest
import dev.kord.rest.json.request.InteractionApplicationCommandCallbackData
import dev.kord.rest.json.request.InteractionResponseCreateRequest
import dev.kord.rest.json.request.ModalResponseCreateRequest
import dev.kord.rest.service.RestClient
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject
import mu.KotlinLogging
import net.perfectdreams.discordinteraktions.common.builder.message.create.InteractionOrFollowupMessageCreateBuilder
import net.perfectdreams.discordinteraktions.common.builder.message.modify.InteractionOrFollowupMessageModifyBuilder
import net.perfectdreams.discordinteraktions.common.entities.messages.EditableMessage
import net.perfectdreams.discordinteraktions.common.requests.InteractionRequestState
import net.perfectdreams.discordinteraktions.common.requests.RequestBridge
import net.perfectdreams.discordinteraktions.common.requests.managers.HttpRequestManager
import net.perfectdreams.discordinteraktions.common.requests.managers.RequestManager
import net.perfectdreams.discordinteraktions.platforms.kord.entities.messages.KordOriginalInteractionEphemeralMessage
import net.perfectdreams.discordinteraktions.platforms.kord.entities.messages.KordOriginalInteractionPublicMessage

/**
 * On this request manager we'll handle the requests
 * by directly interacting with the Discord Rest API.
 *
 * @param rest The application rest client
 * @param applicationId The bot's application id
 * @param interactionToken The request's token
 * @param call The request data
 * @param notificationChannel The notification pipe that we use for notifying events
 */
class WebServerRequestManager(
    bridge: RequestBridge,
    val rest: RestClient,
    val applicationId: Snowflake,
    val interactionToken: String,
    val call: ApplicationCall
) : RequestManager(bridge) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    init {
        require(bridge.state.value == InteractionRequestState.NOT_REPLIED_YET) { "HttpRequestManager should be in the NOT_REPLIED_YET state!" }
    }

    override suspend fun deferChannelMessage() {
        call.respondText(
            buildJsonObject {
                put("type", InteractionResponseType.DeferredChannelMessageWithSource.type)
            }.toString(),
            ContentType.Application.Json
        )

        bridge.state.value = InteractionRequestState.DEFERRED_CHANNEL_MESSAGE

        bridge.manager = HttpRequestManager(
            bridge,
            rest,
            applicationId,
            interactionToken
        )
    }

    override suspend fun deferChannelMessageEphemerally() {
        call.respondText(
            buildJsonObject {
                put("type", InteractionResponseType.DeferredChannelMessageWithSource.type)

                putJsonObject("data") {
                    put("flags", 64)
                }
            }.toString(),
            ContentType.Application.Json
        )

        bridge.state.value = InteractionRequestState.DEFERRED_CHANNEL_MESSAGE

        bridge.manager = HttpRequestManager(
            bridge,
            rest,
            applicationId,
            interactionToken
        )
    }

    override suspend fun sendPublicMessage(message: InteractionOrFollowupMessageCreateBuilder): EditableMessage {
        call.respondText(
            Json.encodeToString(
                InteractionResponseCreateRequest(
                    type = InteractionResponseType.ChannelMessageWithSource,
                    data = Optional(
                        InteractionApplicationCommandCallbackData(
                            content = Optional(message.content).coerceToMissing(),
                            tts = Optional(message.tts).coerceToMissing().toPrimitive(),
                            embeds = Optional(message.embeds?.map { it.toRequest() } ?: listOf()),
                            allowedMentions = Optional(message.allowedMentions).coerceToMissing().map { it.build() },
                            components = message.components?.map { it.build() }.optional().coerceToMissing(),
                            flags = MessageFlags {}.optional()
                        )
                    )
                )
            ),
            ContentType.Application.Json
        )

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED

        bridge.manager = HttpRequestManager(
            bridge,
            rest,
            applicationId,
            interactionToken
        )

        return KordOriginalInteractionPublicMessage(
            rest,
            applicationId,
            interactionToken
        )
    }

    override suspend fun sendEphemeralMessage(message: InteractionOrFollowupMessageCreateBuilder): EditableMessage {
        call.respondText(
            Json.encodeToString(
                InteractionResponseCreateRequest(
                    type = InteractionResponseType.ChannelMessageWithSource,
                    data = Optional(
                        InteractionApplicationCommandCallbackData(
                            content = Optional(message.content).coerceToMissing(),
                            tts = Optional(message.tts).coerceToMissing().toPrimitive(),
                            embeds = Optional(message.embeds?.map { it.toRequest() }).coerceToMissing(),
                            allowedMentions = Optional(message.allowedMentions).coerceToMissing().map { it.build() },
                            components = message.components?.map { it.build() }.optional().coerceToMissing(),
                            flags = MessageFlags {
                                +MessageFlag.Ephemeral
                            }.optional()
                        )
                    )
                )
            ),
            ContentType.Application.Json
        )

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED

        bridge.manager = HttpRequestManager(
            bridge,
            rest,
            applicationId,
            interactionToken
        )

        return KordOriginalInteractionEphemeralMessage(
            rest,
            applicationId,
            interactionToken
        )
    }

    override suspend fun deferUpdateMessage() {
        call.respondText(
            buildJsonObject {
                put("type", InteractionResponseType.DeferredUpdateMessage.type)
            }.toString(),
            ContentType.Application.Json
        )

        bridge.state.value = InteractionRequestState.DEFERRED_UPDATE_MESSAGE

        bridge.manager = HttpRequestManager(
            bridge,
            rest,
            applicationId,
            interactionToken
        )
    }

    override suspend fun updateMessage(message: InteractionOrFollowupMessageModifyBuilder): EditableMessage {
        call.respondText(
            Json.encodeToString(
                InteractionResponseCreateRequest(
                    type = InteractionResponseType.UpdateMessage,
                    data = Optional(
                        InteractionApplicationCommandCallbackData(
                            content = Optional(message.content).coerceToMissing(),
                            embeds = Optional(message.embeds?.map { it.toRequest() } ?: listOf()),
                            allowedMentions = Optional(message.allowedMentions?.build()).coerceToMissing(),
                            components = message.components?.map { it.build() }.optional().coerceToMissing(),
                            flags = MessageFlags {}.optional()
                        )
                    )
                )
            ),
            ContentType.Application.Json
        )

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED

        bridge.manager = HttpRequestManager(
            bridge,
            rest,
            applicationId,
            interactionToken
        )

        return KordOriginalInteractionPublicMessage(
            rest,
            applicationId,
            interactionToken
        )
    }

    override suspend fun sendStringAutocomplete(list: List<Choice<String>>) {
        call.respondText(
            Json.encodeToString(
                AutoCompleteResponseCreateRequest(
                    InteractionResponseType.ApplicationCommandAutoCompleteResult,
                    DiscordAutoComplete(list)
                )
            ),
            ContentType.Application.Json
        )

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED
    }

    override suspend fun sendIntegerAutocomplete(list: List<Choice<Long>>) {
        call.respondText(
            Json.encodeToString(
                AutoCompleteResponseCreateRequest(
                    InteractionResponseType.ApplicationCommandAutoCompleteResult,
                    DiscordAutoComplete(list)
                )
            ),
            ContentType.Application.Json
        )

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED
    }

    override suspend fun sendNumberAutocomplete(list: List<Choice<Double>>) {
        call.respondText(
            Json.encodeToString(
                AutoCompleteResponseCreateRequest(
                    InteractionResponseType.ApplicationCommandAutoCompleteResult,
                    DiscordAutoComplete(list)
                )
            ),
            ContentType.Application.Json
        )

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED
    }

    override suspend fun sendForm(title: String, customId: String, builder: ModalBuilder.() -> Unit) {
        call.respondText(
            Json.encodeToString(
                ModalResponseCreateRequest(
                    InteractionResponseType.Modal,
                    ModalBuilder(title, customId).apply(builder).toRequest()
                )
            ),
            ContentType.Application.Json
        )

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED
    }
}