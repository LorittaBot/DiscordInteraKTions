package net.perfectdreams.discordinteraktions.platforms.kord.commands

import dev.kord.common.entity.Snowflake
import dev.kord.rest.builder.interaction.ApplicationCommandCreateBuilder
import dev.kord.rest.builder.interaction.BaseInputChatBuilder
import dev.kord.rest.builder.interaction.ChatInputCreateBuilder
import dev.kord.rest.builder.interaction.GroupCommandBuilder
import dev.kord.rest.builder.interaction.MessageCommandCreateBuilder
import dev.kord.rest.builder.interaction.SubCommandBuilder
import dev.kord.rest.builder.interaction.UserCommandCreateBuilder
import dev.kord.rest.builder.interaction.attachment
import dev.kord.rest.builder.interaction.boolean
import dev.kord.rest.builder.interaction.channel
import dev.kord.rest.builder.interaction.int
import dev.kord.rest.builder.interaction.number
import dev.kord.rest.builder.interaction.role
import dev.kord.rest.builder.interaction.string
import dev.kord.rest.builder.interaction.user
import dev.kord.rest.service.RestClient
import net.perfectdreams.discordinteraktions.common.commands.ApplicationCommandDeclaration
import net.perfectdreams.discordinteraktions.common.commands.CommandManager
import net.perfectdreams.discordinteraktions.common.commands.CommandRegistry
import net.perfectdreams.discordinteraktions.common.commands.MessageCommandDeclaration
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandDeclaration
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandGroupDeclaration
import net.perfectdreams.discordinteraktions.common.commands.UserCommandDeclaration
import net.perfectdreams.discordinteraktions.common.commands.options.AttachmentCommandOption
import net.perfectdreams.discordinteraktions.common.commands.options.BooleanCommandOption
import net.perfectdreams.discordinteraktions.common.commands.options.ChannelCommandOption
import net.perfectdreams.discordinteraktions.common.commands.options.ChoiceableCommandOption
import net.perfectdreams.discordinteraktions.common.commands.options.CommandOption
import net.perfectdreams.discordinteraktions.common.commands.options.IntegerCommandOption
import net.perfectdreams.discordinteraktions.common.commands.options.NullableAttachmentCommandOption
import net.perfectdreams.discordinteraktions.common.commands.options.NullableBooleanCommandOption
import net.perfectdreams.discordinteraktions.common.commands.options.NullableChannelCommandOption
import net.perfectdreams.discordinteraktions.common.commands.options.NullableCommandOption
import net.perfectdreams.discordinteraktions.common.commands.options.NullableIntegerCommandOption
import net.perfectdreams.discordinteraktions.common.commands.options.NullableNumberCommandOption
import net.perfectdreams.discordinteraktions.common.commands.options.NullableRoleCommandOption
import net.perfectdreams.discordinteraktions.common.commands.options.NullableStringCommandOption
import net.perfectdreams.discordinteraktions.common.commands.options.NullableUserCommandOption
import net.perfectdreams.discordinteraktions.common.commands.options.NumberCommandOption
import net.perfectdreams.discordinteraktions.common.commands.options.RoleCommandOption
import net.perfectdreams.discordinteraktions.common.commands.options.StringCommandOption
import net.perfectdreams.discordinteraktions.common.commands.options.UserCommandOption

class KordCommandRegistry(private val applicationId: Snowflake, private val rest: RestClient, private val manager: CommandManager) : CommandRegistry {
    override suspend fun updateAllCommandsInGuild(guildId: Snowflake, deleteUnknownCommands: Boolean) {
        if (deleteUnknownCommands) {
            // Check commands that are already registered and remove the ones that aren't present in our command manager
            val alreadyRegisteredCommands = rest.interaction.getGuildApplicationCommands(
                applicationId,
                guildId
            )

            val alreadyRegisteredCommandLabels = manager.declarations.map { it.name }

            val commandsToBeRemoved = alreadyRegisteredCommands.filter { it.name !in alreadyRegisteredCommandLabels }

            commandsToBeRemoved.forEach {
                rest.interaction.deleteGuildApplicationCommand(
                    applicationId,
                    guildId,
                    it.id
                )
            }
        }

        rest.interaction.createGuildApplicationCommands(
            applicationId,
            guildId,
            manager.declarations.map {
                convertCommandDeclarationToKord(it).toRequest()
            }
        )
    }

    override suspend fun updateAllGlobalCommands(deleteUnknownCommands: Boolean) {
        val kordApplicationId = applicationId

        if (deleteUnknownCommands) {
            // Check commands that are already registered and remove the ones that aren't present in our command manager
            val alreadyRegisteredCommands = rest.interaction.getGlobalApplicationCommands(kordApplicationId)

            val alreadyRegisteredCommandLabels = manager.declarations.map { it.name }

            val commandsToBeRemoved = alreadyRegisteredCommands.filter { it.name !in alreadyRegisteredCommandLabels }

            commandsToBeRemoved.forEach {
                rest.interaction.deleteGlobalApplicationCommand(
                    kordApplicationId,
                    it.id
                )
            }
        }

        rest.interaction.createGlobalApplicationCommands(
            kordApplicationId,
            manager.declarations.map {
                convertCommandDeclarationToKord(it).toRequest()
            }
        )
    }

    private fun convertCommandDeclarationToKord(declaration: ApplicationCommandDeclaration): ApplicationCommandCreateBuilder {
        when (declaration) {
            is UserCommandDeclaration -> {
                return UserCommandCreateBuilder(declaration.name)
            }

            is MessageCommandDeclaration -> {
                return MessageCommandCreateBuilder(declaration.name)
            }

            is SlashCommandDeclaration -> {
                val commandData = ChatInputCreateBuilder(declaration.name, declaration.description)
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
                val commandData = ChatInputCreateBuilder(declaration.name, declaration.description)
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

    private fun convertCommandOptionToKord(cmdOption: CommandOption<*>, builder: BaseInputChatBuilder) {
        when (cmdOption) {
            // TODO: Add all possible types
            is IntegerCommandOption, is NullableIntegerCommandOption ->
                if (cmdOption is ChoiceableCommandOption<*, *>) {
                    builder.int(cmdOption.name, cmdOption.description) {
                        this.required = cmdOption !is NullableCommandOption
                        this.autocomplete = cmdOption.autoCompleteExecutorDeclaration != null

                        for (choice in cmdOption.choices) {
                            choice(choice.name, choice.value as Long)
                        }
                    }
                } else error("The $cmdOption should be choiceable, but it isn't! Bug?")
            is NumberCommandOption, is NullableNumberCommandOption ->
                if (cmdOption is ChoiceableCommandOption<*, *>) {
                    builder.number(cmdOption.name, cmdOption.description) {
                        this.required = cmdOption !is NullableCommandOption
                        this.autocomplete = cmdOption.autoCompleteExecutorDeclaration != null

                        for (choice in cmdOption.choices) {
                            choice(choice.name, choice.value as Double)
                        }
                    }
                } else error("The $cmdOption should be choiceable, but it isn't! Bug?")
            is StringCommandOption, is NullableStringCommandOption ->
                if (cmdOption is ChoiceableCommandOption<*, *>) {
                    builder.string(cmdOption.name, cmdOption.description) {
                        this.required = cmdOption !is NullableCommandOption
                        this.autocomplete = cmdOption.autoCompleteExecutorDeclaration != null

                        for (choice in cmdOption.choices) {
                            choice(choice.name, choice.value as String)
                        }
                    }
                } else error("The $cmdOption should be choiceable, but it isn't! Bug?")
            is BooleanCommandOption, is NullableBooleanCommandOption ->
                builder.boolean(cmdOption.name, cmdOption.description) {
                    this.required = cmdOption !is NullableCommandOption
                }
            is UserCommandOption, is NullableUserCommandOption ->
                builder.user(cmdOption.name, cmdOption.description) {
                    this.required = cmdOption !is NullableCommandOption
                }
            is ChannelCommandOption, is NullableChannelCommandOption ->
                builder.channel(cmdOption.name, cmdOption.description) {
                    this.required = cmdOption !is NullableCommandOption
                }
            is RoleCommandOption, is NullableRoleCommandOption ->
                builder.role(cmdOption.name, cmdOption.description) {
                    this.required = cmdOption !is NullableCommandOption
                }
            is AttachmentCommandOption, is NullableAttachmentCommandOption ->
                builder.attachment(cmdOption.name, cmdOption.description) {
                    this.required = cmdOption !is NullableCommandOption
                }
            else -> error("Unsupported type ${cmdOption::class}")
        }
    }
}