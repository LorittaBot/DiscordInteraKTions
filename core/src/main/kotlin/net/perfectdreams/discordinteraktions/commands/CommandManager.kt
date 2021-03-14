package net.perfectdreams.discordinteraktions.commands

import mu.KotlinLogging
import net.perfectdreams.discordinteraktions.InteractionsServer

class CommandManager(val m: InteractionsServer) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    val commands = mutableListOf<SlashCommand>()

    /* suspend fun updateAllGlobalCommands() {}

    suspend fun updateAllCommandsInGuild(
        guildId: Long,
        removeUnknownCommands: Boolean = true
    ) {
        val response = m.http.get<String>("https://discord.com/api/v8/applications/${m.applicationId}/guilds/$guildId/commands") {
            header("Authorization", "Bot ${m.token}")
            header("User-Agent", "Discord InteraKTions (Application ID: ${m.applicationId})")
        }

        println(response)

        val registeredCommands = InteractionsServer.json.decodeFromString<List<GuildApplicationCommand>>(response)

        if (removeUnknownCommands) {
            // This will remove all commands that do not match any label of the currently registered commands
            // We don't need to remove commands that have the same label because the PUT request will replace them automatically
            val allCommandsLabels = commands.map { it.label }

            val commandsToBeRemoved = registeredCommands
                .filter {
                    it.name !in allCommandsLabels
                }

            commandsToBeRemoved.forEach {
                logger.info { "Removing command ${it.name} because there isn't any matching registered command..." }

                m.http.delete<String>("https://discord.com/api/v8/applications/${m.applicationId}/guilds/$guildId/commands/${it.id}") {
                    header("Authorization", "Bot ${m.token}")
                    header("User-Agent", DKTConstants.USER_AGENT.format(m.applicationId))
                }
            }
        }

        // Create command objects
        val commandsToBeRegistered = commands.map {
            val options = it.arguments.map {
                ApplicationCommandOption(
                    it.type,
                    it.name,
                    it.description,
                    it.required,
                    it.choices.map {
                        ApplicationCommandOptionChoice(
                            it.name,
                            it.value
                        )
                    },
                    null
                )
            }

            ApplicationCommandBase(
                it.label,
                it.description,
                options
            )
        }

        val response2 = m.http.put<String>("https://discord.com/api/v8/applications/${m.applicationId}/guilds/297732013006389252/commands") {
            header("Authorization", "Bot ${m.token}")
            header("User-Agent", DKTConstants.USER_AGENT.format(m.applicationId))

            body = TextContent(InteractionsServer.json.encodeToString(commandsToBeRegistered), ContentType.Application.Json)
        }

        println(response2)
    } */
}