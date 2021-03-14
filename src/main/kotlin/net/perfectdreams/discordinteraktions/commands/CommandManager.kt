package net.perfectdreams.discordinteraktions.commands

import io.ktor.client.request.*
import io.ktor.content.*
import io.ktor.http.*
import kotlinx.serialization.encodeToString
import net.perfectdreams.discordinteraktions.InteractionsServer

class CommandManager(val m: InteractionsServer) {
    val commands = mutableListOf<InteractionCommand>()

    fun registerCommand(command: InteractionCommand) {
        this.commands.add(command)
    }

    fun registerCommands(vararg commands: InteractionCommand) {
        commands.forEach { registerCommand(it) }
    }

    suspend fun updateAllCommands() {
        val response = m.http.get<String>("https://discord.com/api/v8/applications/${m.applicationId}/guilds/297732013006389252/commands") {
            header("Authorization", "Bot ${m.token}")
            header("User-Agent", "Discord InteraKTions (Application ID: ${m.applicationId})")
        }

        println(response)

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

            ApplicationCommand(
                it.label,
                it.description,
                options
            )
        }

        val response2 = m.http.put<String>("https://discord.com/api/v8/applications/${m.applicationId}/guilds/297732013006389252/commands") {
            header("Authorization", "Bot ${m.token}")
            header("User-Agent", "Discord InteraKTions (Application ID: ${m.applicationId})")

            body = TextContent(InteractionsServer.json.encodeToString(commandsToBeRegistered), ContentType.Application.Json)
        }

        println(response2)
    }
}