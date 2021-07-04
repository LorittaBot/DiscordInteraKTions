package net.perfectdreams.discordinteraktions.platforms.kord

import dev.kord.common.entity.DiscordInteraction
import dev.kord.gateway.DefaultGateway
import dev.kord.gateway.start
import dev.kord.rest.service.RestClient
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.perfectdreams.discordinteraktions.api.entities.Snowflake
import net.perfectdreams.discordinteraktions.common.commands.CommandManager
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandExecutor
import net.perfectdreams.discordinteraktions.common.context.InteractionRequestState
import net.perfectdreams.discordinteraktions.common.context.RequestBridge
import net.perfectdreams.discordinteraktions.common.context.commands.GuildSlashCommandContext
import net.perfectdreams.discordinteraktions.common.context.commands.SlashCommandArguments
import net.perfectdreams.discordinteraktions.common.context.commands.SlashCommandContext
import net.perfectdreams.discordinteraktions.common.utils.Observable
import net.perfectdreams.discordinteraktions.declarations.slash.SlashCommandDeclaration
import net.perfectdreams.discordinteraktions.declarations.slash.SlashCommandExecutorDeclaration
import net.perfectdreams.discordinteraktions.declarations.slash.options.CommandOptions
import net.perfectdreams.discordinteraktions.declarations.slash.slashCommand
import net.perfectdreams.discordinteraktions.platforms.kord.commands.CommandDeclarationUtils
import net.perfectdreams.discordinteraktions.platforms.kord.commands.KordCommandRegistry
import net.perfectdreams.discordinteraktions.platforms.kord.context.manager.InitialHttpRequestManager
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordMember
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordUser
import net.perfectdreams.discordinteraktions.platforms.kord.utils.toKordSnowflake
import java.io.File

suspend fun main() {
    val rest = RestClient(File("token.txt").readText())
    val commandManager = CommandManager()

    commandManager.register(TestCommand, TestCommandExecutor())

    val registry = KordCommandRegistry(
        Snowflake(744361365724069898L),
        rest,
        commandManager
    )

    registry.updateAllCommandsInGuild(Snowflake(297732013006389252L), false)

    embeddedServer(Netty, port = 8080) {
        routing {
            post("/") {
                try {
                    val payload = call.receiveText()

                    println("Received $payload!")

                    // Decode the payload
                    val request = Json { ignoreUnknownKeys = true }.decodeFromString<DiscordInteraction>(payload)

                    println("interaction type: ${request.type}")

                    // Processing subcommands is kinda hard, but not impossible!
                    val commandLabels = CommandDeclarationUtils.findAllSubcommandDeclarationNames(request)
                    val relativeOptions = CommandDeclarationUtils.getNestedOptions(request.data.options.value)

                    println("Subcommand Labels: $commandLabels; Root Options: $relativeOptions")

                    val command = commandManager.declarations
                        .asSequence()
                        .mapNotNull {
                            CommandDeclarationUtils.getLabelsConnectedToCommandDeclaration(
                                commandLabels,
                                it
                            )
                        }
                        .first()

                    val executorDeclaration = command.executor ?: return@post
                    val executor = commandManager.executors.first {
                        it::class == executorDeclaration.parent
                    }

                    // Convert the Nested Options into a map, then we can access them with our Discord InteraKTion options!
                    val arguments = convertOptions(
                        request,
                        executorDeclaration,
                        relativeOptions ?: listOf()
                    )

                    val observableState = Observable(InteractionRequestState.NOT_REPLIED_YET)
                    val bridge = RequestBridge(observableState)

                    val requestManager = InitialHttpRequestManager(
                        bridge,
                        rest,
                        Snowflake(744361365724069898L).toKordSnowflake(),
                        request.token,
                        request
                    )

                    bridge.manager = requestManager

                    val kordUser = KordUser(request.member.value?.user?.value ?: request.user.value ?: error("oh no"))
                    val guildId =
                        request.guildId.value?.let { net.perfectdreams.discordinteraktions.api.entities.Snowflake(it.value) }

                    // If the guild ID is not null, then it means that the interaction happened in a guild!
                    val commandContext = if (guildId != null) {
                        val kordMember = KordMember(
                            request.member.value!! // Should NEVER be null!
                        )

                        GuildSlashCommandContext(
                            bridge,
                            kordUser,
                            guildId,
                            kordMember
                        )
                    } else {
                        SlashCommandContext(
                            bridge,
                            KordUser(
                                request.member.value?.user?.value ?: request.user.value ?: error("oh no")
                            )
                        )
                    }

                    GlobalScope.launch {
                        println("Executing command...")
                        executor.execute(
                            commandContext,
                            SlashCommandArguments(
                                arguments
                            )
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }.start(wait = true)
}