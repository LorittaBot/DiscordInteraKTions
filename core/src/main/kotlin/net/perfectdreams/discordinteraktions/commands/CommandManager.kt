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
import net.perfectdreams.discordinteraktions.declarations.slash.options.CommandOption
import net.perfectdreams.discordinteraktions.declarations.slash.SlashCommandDeclaration
import net.perfectdreams.discordinteraktions.declarations.slash.SlashCommandDeclarationBuilder
import net.perfectdreams.discordinteraktions.declarations.slash.SlashCommandGroupDeclarationBuilder
import net.perfectdreams.discordinteraktions.declarations.slash.options.CommandOptionType

class CommandManager(
    val rest: RestClient,
    val applicationId: Snowflake
) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    val declarations = mutableListOf<SlashCommandDeclarationBuilder>()
    val executors = mutableListOf<SlashCommandExecutor>()

    fun register(declaration: SlashCommandDeclaration, vararg executors: SlashCommandExecutor) {
        declarations.add(declaration.declaration())
        this.executors.addAll(executors)
    }

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
            val allCommandsLabels = currentlyRegisteredCommands.map { it.name }
            val allDeclaredLabels = declarations.map { it.name }

            val commandsToBeRemoved = allCommandsLabels - allDeclaredLabels

            commandsToBeRemoved.forEach {
                val command = currentlyRegisteredCommands.first { registeredCommand ->
                    registeredCommand.name == it
                }

                logger.info { "Deleting command $it (${command.id}) because there isn't any matching registered command..." }

                rest.interaction.deleteGuildApplicationCommand(
                    applicationId,
                    guildId,
                    command.id
                )
            }
        }

        val commandsToBeRegistered = mutableListOf<ApplicationCommandCreateRequest>()

        for (slash in declarations) {
            commandsToBeRegistered.add(convertDeclarationToKord(slash))
        }

        rest.interaction.createGuildApplicationCommands(
            applicationId,
            guildId,
            commandsToBeRegistered
        )
    }

    /* suspend fun updateAllGlobalCommands(
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
    } */

    private fun convertDeclarationToKordSubCommandOption(declaration: SlashCommandDeclarationBuilder): ApplicationCommandOption {
        return ApplicationCommandOption(
            ApplicationCommandOptionType.SubCommand,
            declaration.name,
            declaration.description ?: throw IllegalArgumentException("Can't have a empty description!"),
            OptionalBoolean.Missing,
            OptionalBoolean.Missing,
            Optional.Missing(),
            (declaration.executor?.options?.arguments?.map { convertOptionToKordOption(it) } ?: listOf()).optional()
        )
    }

    private fun convertDeclarationToKordSubCommandGroupOption(declaration: SlashCommandGroupDeclarationBuilder): ApplicationCommandOption {
        return ApplicationCommandOption(
            ApplicationCommandOptionType.SubCommandGroup,
            declaration.name,
            declaration.description ?: throw IllegalArgumentException("Can't have a empty description!"),
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
            if (it.type == CommandOptionType.String || it.type == CommandOptionType.NullableString) {
                Choice.StringChoice(it.name, it.value as String)
            } else if (it.type == CommandOptionType.Integer || it.type == CommandOptionType.NullableInteger) {
                Choice.IntChoice(it.name, it.value as Int)
            } else throw IllegalArgumentException("I don't know how to handle a $it!")
        }

        return ApplicationCommandOption(
            when (option.type) {
                CommandOptionType.Integer, CommandOptionType.NullableInteger -> ApplicationCommandOptionType.Integer
                CommandOptionType.String, CommandOptionType.NullableString -> ApplicationCommandOptionType.String
                CommandOptionType.Bool, CommandOptionType.NullableBool -> ApplicationCommandOptionType.Boolean
                CommandOptionType.User, CommandOptionType.NullableUser -> ApplicationCommandOptionType.User
                CommandOptionType.Channel, CommandOptionType.NullableChannel -> ApplicationCommandOptionType.Channel
                CommandOptionType.Role, CommandOptionType.NullableRole -> ApplicationCommandOptionType.Role
                else -> throw UnsupportedOperationException("I don't know how to handle ${option.type}!")
            },
            option.name,
            option.description,
            required = OptionalBoolean.Value(option.type !is CommandOptionType.Nullable),
            choices = choices.optional()
        )
    }

    private fun convertDeclarationToKord(declaration: SlashCommandDeclarationBuilder): ApplicationCommandCreateRequest {
        // We can only have (subcommands OR subcommand groups) OR arguments
        val arguments = if (declaration.subcommands.isNotEmpty() || declaration.subcommandGroups.isNotEmpty()) {
            declaration.subcommands.map {
                convertDeclarationToKordSubCommandOption(it)
            } + declaration.subcommandGroups.map {
                convertDeclarationToKordSubCommandGroupOption(it)
            }
        } else {
            val arguments = declaration.executor?.options?.arguments ?: listOf()

            // A very weird code that converts our declarations to Kord's command declarations
            arguments.map {
                convertOptionToKordOption(it)
            }
        }

        return ApplicationCommandCreateRequest(
            declaration.name,
            declaration.description ?: throw IllegalArgumentException("Can't have a empty description!"),
            Optional(arguments)
        )
    }
}