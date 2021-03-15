package net.perfectdreams.discordinteraktions

import dev.kord.common.entity.InteractionResponseType
import dev.kord.common.entity.Snowflake
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.receiveOrNull
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import mu.KotlinLogging
import net.perfectdreams.discordinteraktions.context.GuildSlashCommandContext
import net.perfectdreams.discordinteraktions.context.HttpRequestManager
import net.perfectdreams.discordinteraktions.context.SlashCommandContext
import net.perfectdreams.discordinteraktions.context.WebServerRequestManager
import net.perfectdreams.discordinteraktions.entities.CommandInteraction
import net.perfectdreams.discordinteraktions.entities.PingInteraction

class DefaultInteractionRequestHandler(val m: InteractionsServer) : InteractionRequestHandler() {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    override suspend fun onPing(call: ApplicationCall, request: PingInteraction) {
        logger.info { "Ping Request Received! Triggered by ${request.user}" }
        call.respondText(
            buildJsonObject {
                put("type", InteractionResponseType.Pong.type)
            }.toString(),
            ContentType.Application.Json
        )
    }

    override suspend fun onCommand(call: ApplicationCall, request: CommandInteraction) {
        println(request.data.name)

        val command = m.commandManager.commands.first { it.declaration.name == request.data.name }

        println("Command: $command")

        val notificationChannel = Channel<Unit>(0)
        val requestManager = WebServerRequestManager(
            m.rest,
            Snowflake(m.applicationId),
            request.token,
            call,
            notificationChannel
        )

        val commandContext = if (request.guildId.value != null) {
            GuildSlashCommandContext(
                request,
                requestManager
            )
        } else {
            SlashCommandContext(
                request,
                WebServerRequestManager(
                    m.rest,
                    Snowflake(m.applicationId),
                    request.token,
                    call,
                    notificationChannel
                )
            )
        }

        GlobalScope.launch {
            command.executes(commandContext)
            println("Finished execution!")
        }

        notificationChannel.receiveOrNull()
        logger.info { "Switching Request Manager..." }
        commandContext.manager = HttpRequestManager(
            m.rest,
            Snowflake(m.applicationId),
            request.token,
            request
        )

        // Send a notification indicating that the code can continue
        notificationChannel.send(Unit)
    }
}