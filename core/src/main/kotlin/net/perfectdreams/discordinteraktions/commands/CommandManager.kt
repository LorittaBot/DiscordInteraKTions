package net.perfectdreams.discordinteraktions.commands

import dev.kord.common.entity.ApplicationCommandOption
import dev.kord.common.entity.ApplicationCommandOptionType
import dev.kord.common.entity.Choice
import dev.kord.common.entity.Snowflake
import dev.kord.common.entity.optional.Optional
import dev.kord.common.entity.optional.OptionalBoolean
import dev.kord.rest.json.request.ApplicationCommandCreateRequest
import dev.kord.rest.service.RestClient
import mu.KotlinLogging
import net.perfectdreams.discordinteraktions.declarations.slash.IntegerCommandChoice
import net.perfectdreams.discordinteraktions.declarations.slash.StringCommandChoice

class CommandManager(
    val rest: RestClient,
    val applicationId: Snowflake
) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    val commands = mutableListOf<SlashCommand>()

    fun register(command: SlashCommand) =
        commands.add(command)

    fun registerAll(vararg commands: SlashCommand) =
        commands.forEach { register(it) }

    fun unregister(command: SlashCommand) =
        commands.remove(command)

    fun unregisterAll(command: SlashCommand) =
        commands.forEach { unregister(it) }

    suspend fun updateAllCommandsInGuild(
        guildId: Snowflake,
        deleteUnknownCommands: Boolean = true
    ) {
        if (deleteUnknownCommands) {
            // If we want to check if we should delete commands, we need to get them first!
            val currentlyRegisteredCommands = rest.interaction.getGuildApplicationCommands(
                applicationId,
                guildId
            )

            // This will remove all commands that do not match any label of the currently registered commands
            // We don't need to remove commands that have the same label because the PUT request will replace them automatically
            val allCommandsLabels = commands.map { it.declaration.name }

            val commandsToBeRemoved = commands
                .filter {
                    it.declaration.name !in allCommandsLabels
                }

            commandsToBeRemoved.forEach {
                val command = currentlyRegisteredCommands.first { registeredCommand ->
                    registeredCommand.name == it.declaration.name
                }

                logger.info { "Deleting command ${it.declaration.name} (${command.id}) because there isn't any matching registered command..." }

                rest.interaction.deleteGuildApplicationCommand(
                    applicationId,
                    guildId,
                    command.id
                )
            }
        }

        val commandsToBeRegistered = mutableListOf<ApplicationCommandCreateRequest>()

        for (slash in commands) {
            // A very weird code that converts our declarations to Kord's command declarations
            val kordArguments = slash.declaration.options.arguments.map {
                val choices = it.choices.map {
                    if (it is StringCommandChoice) {
                        Choice.StringChoice(it.name, it.value)
                    } else if (it is IntegerCommandChoice) {
                        Choice.IntChoice(it.name, it.value)
                    } else throw IllegalArgumentException("I don't know how to handle a $it!")
                }

                ApplicationCommandOption(
                    ApplicationCommandOptionType.Unknown(it.type),
                    it.name,
                    it.description,
                    required = OptionalBoolean.Value(it.required),
                    choices = Optional(choices)
                )
            }

            commandsToBeRegistered.add(
                ApplicationCommandCreateRequest(
                    slash.declaration.name,
                    slash.declaration.description,
                    Optional(kordArguments)
                )
            )
        }

        rest.interaction.createGuildApplicationCommands(
            applicationId,
            guildId,
            commandsToBeRegistered
        )
    }
}