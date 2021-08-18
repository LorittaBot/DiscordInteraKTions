package net.perfectdreams.discordinteraktions.platforms.kord.commands

import dev.kord.rest.builder.interaction.ApplicationCommandCreateBuilder
import dev.kord.rest.builder.interaction.BaseApplicationBuilder
import dev.kord.rest.builder.interaction.GroupCommandBuilder
import dev.kord.rest.builder.interaction.SubCommandBuilder
import dev.kord.rest.service.RestClient
import net.perfectdreams.discordinteraktions.api.entities.Snowflake
import net.perfectdreams.discordinteraktions.common.commands.CommandManager
import net.perfectdreams.discordinteraktions.common.commands.CommandRegistry
import net.perfectdreams.discordinteraktions.declarations.commands.ApplicationCommandDeclaration
import net.perfectdreams.discordinteraktions.declarations.commands.InteractionCommandDeclaration
import net.perfectdreams.discordinteraktions.declarations.commands.SlashCommandDeclaration
import net.perfectdreams.discordinteraktions.declarations.commands.SlashCommandGroupDeclaration
import net.perfectdreams.discordinteraktions.declarations.commands.slash.options.CommandOption
import net.perfectdreams.discordinteraktions.declarations.commands.slash.options.CommandOptionType
import net.perfectdreams.discordinteraktions.platforms.kord.utils.toKordSnowflake

class KordCommandRegistry(private val applicationId: Snowflake, private val rest: RestClient, private val manager: CommandManager) : CommandRegistry {
    override suspend fun updateAllCommandsInGuild(guildId: Snowflake, deleteUnknownCommands: Boolean) {
        val kordApplicationId = applicationId.toKordSnowflake()
        val kordGuildId = guildId.toKordSnowflake()

        if (deleteUnknownCommands) {
            // Check commands that are already registered and remove the ones that aren't present in our command manager
            val alreadyRegisteredCommands = rest.interaction.getGuildApplicationCommands(
                kordApplicationId,
                kordGuildId
            )

            val alreadyRegisteredCommandLabels = manager.declarations.map { it.name }

            val commandsToBeRemoved = alreadyRegisteredCommands.filter { it.name !in alreadyRegisteredCommandLabels }

            commandsToBeRemoved.forEach {
                rest.interaction.deleteGuildApplicationCommand(
                    kordApplicationId,
                    kordGuildId,
                    it.id
                )
            }
        }

        rest.interaction.createGuildApplicationCommands(
            kordApplicationId,
            kordGuildId,
            manager.declarations.map {
                convertCommandDeclarationToKord(it).toRequest()
            }
        )
    }

    override suspend fun updateAllGlobalCommands(deleteUnknownCommands: Boolean) {
        // TODO: Remove unknown commands
    }

    private fun convertCommandDeclarationToKord(declaration: InteractionCommandDeclaration): ApplicationCommandCreateBuilder {
        when (declaration) {
            is ApplicationCommandDeclaration -> TODO()
            is SlashCommandDeclaration -> {
                val commandData = ApplicationCommandCreateBuilder(declaration.name, declaration.description)
                commandData.options = mutableListOf() // Initialize a empty list so we can use it

                // We can only have (subcommands OR subcommand groups) OR arguments
                if (declaration.subcommands.isNotEmpty() || declaration.subcommandGroups.isNotEmpty()) {
                    declaration.subcommands.forEach {
                        commandData.options?.add(convertSubcommandDeclarationToKord(it))
                    }

                    declaration.subcommandGroups.forEach {
                        commandData.options?.add(convertSubcommandGroupDeclarationToKord(it))
                    }
                } else {
                    val executor = declaration.executor ?: error("Root command without a executor!")

                    val options = executor.options

                    options.arguments.forEach {
                        convertCommandOptionToKord(it, commandData)
                    }
                }

                return commandData
            }
            is SlashCommandGroupDeclaration -> {
                val commandData = ApplicationCommandCreateBuilder(declaration.name, declaration.description)
                commandData.options = mutableListOf() // Initialize a empty list so we can use it

                declaration.subcommands.forEach {
                    commandData.options?.add(convertSubcommandDeclarationToKord(it))
                }

                return commandData
            }
        }
    }

    private fun convertSubcommandDeclarationToKord(declaration: SlashCommandDeclaration): SubCommandBuilder {
        val commandData = SubCommandBuilder(declaration.name, declaration.description)

        // This is a subcommand, so we only have a executor anyway
        val executor = declaration.executor ?: error("Subcommand without a executor!")
        val options = executor.options

        options.arguments.forEach {
            convertCommandOptionToKord(it, commandData)
        }

        return commandData
    }

    private fun convertSubcommandGroupDeclarationToKord(declaration: SlashCommandGroupDeclaration): GroupCommandBuilder {
        val commandData = GroupCommandBuilder(declaration.name, declaration.description)
        commandData.options = mutableListOf() // Initialize a empty list so we can use it

        declaration.subcommands.forEach {
            commandData.options?.add(convertSubcommandDeclarationToKord(it))
        }

        return commandData
    }

    private fun convertCommandOptionToKord(cmdOption: CommandOption<*>, builder: BaseApplicationBuilder) {
        when (cmdOption.type) {
            // TODO: Add all possible types
            CommandOptionType.Integer, CommandOptionType.NullableInteger ->
                builder.int(cmdOption.name, cmdOption.description) {
                    this.required = !cmdOption.type.isNullable

                    for (choice in cmdOption.choices) {
                        choice(choice.name, choice.value as Int)
                    }
                }
            CommandOptionType.Number, CommandOptionType.NullableNumber ->
                builder.number(cmdOption.name, cmdOption.description) {
                    this.required = !cmdOption.type.isNullable

                    for (choice in cmdOption.choices) {
                        choice(choice.name, choice.value as Double)
                    }
                }
            CommandOptionType.String, CommandOptionType.NullableString ->
                builder.string(cmdOption.name, cmdOption.description) {
                    this.required = !cmdOption.type.isNullable

                    for (choice in cmdOption.choices) {
                        choice(choice.name, choice.value as String)
                    }
                }
            CommandOptionType.Bool, CommandOptionType.NullableBool ->
                builder.boolean(cmdOption.name, cmdOption.description) {
                    this.required = !cmdOption.type.isNullable
                }
            CommandOptionType.User, CommandOptionType.NullableUser ->
                builder.user(cmdOption.name, cmdOption.description) {
                    this.required = !cmdOption.type.isNullable
                }
            else -> error("Unsupported type ${cmdOption.type}")
        }
    }

    private fun convertCommandOptionToKord(cmdOption: CommandOption<*>, builder: SubCommandBuilder) {
        when (cmdOption.type) {
            // TODO: Add all possible types
            CommandOptionType.Integer, CommandOptionType.NullableInteger ->
                builder.int(cmdOption.name, cmdOption.description) {
                    this.required = !cmdOption.type.isNullable

                    for (choice in cmdOption.choices) {
                        choice(choice.name, choice.value as Int)
                    }
                }
            CommandOptionType.String, CommandOptionType.NullableString ->
                builder.string(cmdOption.name, cmdOption.description) {
                    this.required = !cmdOption.type.isNullable

                    for (choice in cmdOption.choices) {
                        choice(choice.name, choice.value as String)
                    }
                }
            CommandOptionType.Bool, CommandOptionType.NullableBool ->
                builder.boolean(cmdOption.name, cmdOption.description) {
                    this.required = !cmdOption.type.isNullable
                }
            CommandOptionType.User, CommandOptionType.NullableUser ->
                builder.user(cmdOption.name, cmdOption.description) {
                    this.required = !cmdOption.type.isNullable
                }
            else -> error("Unsupported type ${cmdOption.type}")
        }
    }
}