package net.perfectdreams.discordinteraktions.commands

import dev.kord.common.entity.ApplicationCommandOption
import dev.kord.common.entity.ApplicationCommandOptionType
import dev.kord.common.entity.Choice
import dev.kord.common.entity.Snowflake
import dev.kord.common.entity.optional.Optional
import dev.kord.common.entity.optional.OptionalBoolean
import dev.kord.common.entity.optional.optional
import dev.kord.rest.json.request.ApplicationCommandCreateRequest
import dev.kord.rest.service.RestClient
import mu.KotlinLogging
import net.perfectdreams.discordinteraktions.declarations.slash.CommandOption
import net.perfectdreams.discordinteraktions.declarations.slash.IntegerCommandChoice
import net.perfectdreams.discordinteraktions.declarations.slash.SlashCommandDeclaration
import net.perfectdreams.discordinteraktions.declarations.slash.SlashCommandGroupDeclaration
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
            val allCommandsLabels = commands.map { it.rootDeclaration.name }

            val commandsToBeRemoved = commands
                .filter {
                    it.rootDeclaration.name !in allCommandsLabels
                }

            commandsToBeRemoved.forEach {
                val command = currentlyRegisteredCommands.first { registeredCommand ->
                    registeredCommand.name == it.rootDeclaration.name
                }

                logger.info { "Deleting command ${it.rootDeclaration.name} (${command.id}) because there isn't any matching registered command..." }

                rest.interaction.deleteGuildApplicationCommand(
                    applicationId,
                    guildId,
                    command.id
                )
            }
        }

        val commandsToBeRegistered = mutableListOf<ApplicationCommandCreateRequest>()

        // If there are multiple commands, we need to do a distinct check based on the root declaration
        for (slash in commands.distinctBy { it.rootDeclaration }) {
            commandsToBeRegistered.add(convertDeclarationToKord(slash.rootDeclaration))
        }

        rest.interaction.createGuildApplicationCommands(
            applicationId,
            guildId,
            commandsToBeRegistered
        )
    }

    suspend fun updateAllGlobalCommands(
        deleteUnknownCommands: Boolean = true
    ) {
        if (deleteUnknownCommands) {
            // If we want to check if we should delete commands, we need to get them first!
            val currentlyRegisteredCommands = rest.interaction.getGlobalApplicationCommands(
                applicationId
            )

            // This will remove all commands that do not match any label of the currently registered commands
            // We don't need to remove commands that have the same label because the PUT request will replace them automatically
            val allCommandsLabels = commands.map { it.rootDeclaration.name }

            val commandsToBeRemoved = commands
                .filter {
                    it.rootDeclaration.name !in allCommandsLabels
                }

            commandsToBeRemoved.forEach {
                val command = currentlyRegisteredCommands.first { registeredCommand ->
                    registeredCommand.name == it.rootDeclaration.name
                }

                logger.info { "Deleting command ${it.rootDeclaration.name} (${command.id}) because there isn't any matching registered command..." }

                rest.interaction.deleteGlobalApplicationCommand(
                    applicationId,
                    command.id
                )
            }
        }

        val commandsToBeRegistered = mutableListOf<ApplicationCommandCreateRequest>()

        // If there are multiple commands, we need to do a distinct check based on the root declaration
        for (slash in commands.distinctBy { it.rootDeclaration }) {
            commandsToBeRegistered.add(convertDeclarationToKord(slash.rootDeclaration))
        }

        rest.interaction.createGlobalApplicationCommands(
            applicationId,
            commandsToBeRegistered
        )
    }

    private fun convertDeclarationToKordSubCommandOption(declaration: SlashCommandDeclaration): ApplicationCommandOption {
        return ApplicationCommandOption(
            ApplicationCommandOptionType.SubCommand,
            declaration.name,
            declaration.description,
            OptionalBoolean.Missing,
            OptionalBoolean.Missing,
            Optional.Missing(),
            declaration.options.arguments.map { convertOptionToKordOption(it) }.optional()
        )
    }

    private fun convertDeclarationToKordSubCommandGroupOption(declaration: SlashCommandGroupDeclaration): ApplicationCommandOption {
        return ApplicationCommandOption(
            ApplicationCommandOptionType.SubCommandGroup,
            declaration.name,
            declaration.description,
            OptionalBoolean.Missing,
            OptionalBoolean.Missing,
            Optional.Missing(),
            declaration.subcommands.map {
                convertDeclarationToKordSubCommandOption(it)
            }.optional()
        )
    }

    private fun convertOptionToKordOption(option: CommandOption<*>): ApplicationCommandOption {
        val choices = option.choices.map {
            if (it is StringCommandChoice) {
                Choice.StringChoice(it.name, it.value)
            } else if (it is IntegerCommandChoice) {
                Choice.IntChoice(it.name, it.value)
            } else throw IllegalArgumentException("I don't know how to handle a $it!")
        }

        return ApplicationCommandOption(
            ApplicationCommandOptionType.Unknown(option.type),
            option.name,
            option.description,
            required = OptionalBoolean.Value(option.required),
            choices = Optional(choices)
        )
    }

    private fun convertDeclarationToKord(declaration: SlashCommandDeclaration): ApplicationCommandCreateRequest {
        // We can only have (subcommands OR subcommand groups) OR arguments
        val arguments = if (declaration.options.subcommands.isNotEmpty() || declaration.options.subcommandGroups.isNotEmpty()) {
            declaration.options.subcommands.map {
                convertDeclarationToKordSubCommandOption(it)
            } + declaration.options.subcommandGroups.map {
                convertDeclarationToKordSubCommandGroupOption(it)
            }
        } else {
            // A very weird code that converts our declarations to Kord's command declarations
            declaration.options.arguments.map {
                convertOptionToKordOption(it)
            }
        }

        return ApplicationCommandCreateRequest(
            declaration.name,
            declaration.description,
            Optional(arguments)
        )
    }
}