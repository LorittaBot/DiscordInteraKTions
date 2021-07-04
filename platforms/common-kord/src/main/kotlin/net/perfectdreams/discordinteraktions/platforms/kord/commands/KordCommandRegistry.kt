package net.perfectdreams.discordinteraktions.platforms.kord.commands

import dev.kord.rest.builder.interaction.ApplicationCommandCreateBuilder
import dev.kord.rest.builder.interaction.BaseApplicationBuilder
import dev.kord.rest.builder.interaction.GroupCommandBuilder
import dev.kord.rest.builder.interaction.SubCommandBuilder
import dev.kord.rest.service.RestClient
import net.perfectdreams.discordinteraktions.api.entities.Snowflake
import net.perfectdreams.discordinteraktions.common.commands.CommandManager
import net.perfectdreams.discordinteraktions.common.commands.CommandRegistry
import net.perfectdreams.discordinteraktions.declarations.slash.SlashCommandDeclarationBuilder
import net.perfectdreams.discordinteraktions.declarations.slash.SlashCommandGroupDeclarationBuilder
import net.perfectdreams.discordinteraktions.declarations.slash.options.CommandOption
import net.perfectdreams.discordinteraktions.declarations.slash.options.CommandOptionType
import net.perfectdreams.discordinteraktions.platforms.kord.utils.toKordSnowflake

class KordCommandRegistry(private val applicationId: Snowflake, private val rest: RestClient, private val manager: CommandManager) : CommandRegistry {
    override suspend fun updateAllCommandsInGuild(guildId: Snowflake, deleteUnknownCommands: Boolean) {
        // TODO: Remove unknown commands
        rest.interaction.createGuildApplicationCommands(
            applicationId.toKordSnowflake(),
            guildId.toKordSnowflake(),
            manager.declarations.map {
                convertCommandDeclarationToJDA(it).toRequest()
            }
        )
    }

    override suspend fun updateAllGlobalCommands(deleteUnknownCommands: Boolean) {
        // TODO: Remove unknown commands
    }

    private fun convertCommandDeclarationToJDA(declaration: SlashCommandDeclarationBuilder): ApplicationCommandCreateBuilder {
        val commandData = ApplicationCommandCreateBuilder(declaration.name, declaration.description)

        commandData.options = mutableListOf() // Initialize a empty list so we can use it

        // We can only have (subcommands OR subcommand groups) OR arguments
        if (declaration.subcommands.isNotEmpty() || declaration.subcommandGroups.isNotEmpty()) {
            declaration.subcommands.forEach {
                commandData.options?.add(convertSubcommandDeclarationToJDA(it))
            }

            declaration.subcommandGroups.forEach {
                commandData.options?.add(convertSubcommandGroupDeclarationToJDA(it))
            }
        } else {
            val executor = declaration.executor ?: error("Root command without a executor!")

            val options = executor.options

            options.arguments.forEach {
                convertCommandOptionToJDA(it, commandData)
            }
        }

        return commandData
    }

    private fun convertSubcommandDeclarationToJDA(declaration: SlashCommandDeclarationBuilder): SubCommandBuilder {
        val commandData = SubCommandBuilder(declaration.name, declaration.description)

        // This is a subcommand, so we only have a executor anyway
        val executor = declaration.executor ?: error("Subcommand without a executor!")
        val options = executor.options

        options.arguments.forEach {
            convertCommandOptionToJDA(it, commandData)
        }

        return commandData
    }

    private fun convertSubcommandGroupDeclarationToJDA(declaration: SlashCommandGroupDeclarationBuilder): GroupCommandBuilder {
        val commandData = GroupCommandBuilder(declaration.name, declaration.description)

        declaration.subcommands.forEach {
            commandData.options?.add(convertSubcommandDeclarationToJDA(it))
        }

        return commandData
    }

    private fun convertCommandOptionToJDA(cmdOption: CommandOption<*>, builder: BaseApplicationBuilder) {
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

    private fun convertCommandOptionToJDA(cmdOption: CommandOption<*>, builder: SubCommandBuilder) {
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