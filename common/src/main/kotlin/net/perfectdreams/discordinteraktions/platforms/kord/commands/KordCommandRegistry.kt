package net.perfectdreams.discordinteraktions.platforms.kord.commands

import dev.kord.common.entity.Snowflake
import dev.kord.common.entity.optional.optional
import dev.kord.rest.builder.interaction.*
import dev.kord.rest.service.RestClient
import net.perfectdreams.discordinteraktions.common.commands.*
import net.perfectdreams.discordinteraktions.common.commands.options.*

class KordCommandRegistry(private val applicationId: Snowflake, private val rest: RestClient, private val manager: CommandManager) : CommandRegistry {
    override suspend fun updateAllCommandsInGuild(guildId: Snowflake) {
        rest.interaction.createGuildApplicationCommands(
            applicationId,
            guildId
        ) {
            manager.applicationCommandsDeclarations.forEach {
                convertCommandDeclarationToKord(this, it)
            }
        }
    }

    override suspend fun updateAllGlobalCommands() {
        val kordApplicationId = applicationId

        rest.interaction.createGlobalApplicationCommands(
            kordApplicationId
        ) {
            manager.applicationCommandsDeclarations.forEach {
                convertCommandDeclarationToKord(this, it)
            }
        }
    }

    private fun convertCommandDeclarationToKord(builder: MultiApplicationCommandBuilder, declaration: ApplicationCommandDeclaration) {
        // Workaround because Kord's CommandCreateBuilder builders are internal now
        when (declaration) {
            is UserCommandDeclaration -> {
                return builder.user(declaration.name) {
                    nameLocalizations = declaration.nameLocalizations?.toMutableMap()
                }
            }

            is MessageCommandDeclaration -> {
                return builder.message(declaration.name) {
                    nameLocalizations = declaration.nameLocalizations?.toMutableMap()
                }
            }

            is SlashCommandDeclaration -> {
                builder.input(declaration.name, declaration.description) {
                    nameLocalizations = declaration.nameLocalizations?.toMutableMap()
                    descriptionLocalizations = declaration.descriptionLocalizations?.toMutableMap()
                    options = mutableListOf() // Initialize a empty list so we can use it

                    // We can only have (subcommands OR subcommand groups) OR arguments
                    if (declaration.subcommands.isNotEmpty() || declaration.subcommandGroups.isNotEmpty()) {
                        declaration.subcommands.forEach {
                            options?.add(convertSubcommandDeclarationToKord(it))
                        }

                        declaration.subcommandGroups.forEach {
                            options?.add(convertSubcommandGroupDeclarationToKord(it))
                        }
                    } else {
                        val executor = declaration.executor ?: error("Root command without a executor!")

                        val options = executor.options

                        options.arguments.forEach {
                            convertCommandOptionToKord(it, this)
                        }
                    }
                }
            }
            is SlashCommandGroupDeclaration -> error("This should never be called because the convertCommandDeclarationToKord method is only called on a root!")
        }
    }

    private fun convertSubcommandDeclarationToKord(declaration: SlashCommandDeclaration): SubCommandBuilder {
        val commandData = SubCommandBuilder(declaration.name, declaration.description).apply {
            nameLocalizations = declaration.nameLocalizations?.toMutableMap()
            descriptionLocalizations = declaration.descriptionLocalizations?.toMutableMap()
            options = mutableListOf() // Initialize a empty list so we can use it
        }

        // This is a subcommand, so we only have a executor anyway
        val executor = declaration.executor ?: error("Subcommand without a executor!")
        val options = executor.options

        options.arguments.forEach {
            convertCommandOptionToKord(it, commandData)
        }

        return commandData
    }

    private fun convertSubcommandGroupDeclarationToKord(declaration: SlashCommandGroupDeclaration): GroupCommandBuilder {
        val commandData = GroupCommandBuilder(declaration.name, declaration.description).apply {
            nameLocalizations = declaration.nameLocalizations?.toMutableMap()
            descriptionLocalizations = declaration.descriptionLocalizations?.toMutableMap()
            options = mutableListOf() // Initialize a empty list so we can use it
        }
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
                        this.nameLocalizations = cmdOption.nameLocalizations?.toMutableMap()
                        this.descriptionLocalizations = cmdOption.descriptionLocalizations?.toMutableMap()
                        this.required = cmdOption !is NullableCommandOption
                        this.autocomplete = cmdOption.autoCompleteExecutorDeclaration != null

                        val numericOption = (cmdOption as NumericCommandOption<*, *>)
                        this.minValue = numericOption.minValue as Long
                        this.maxValue = numericOption.maxValue as Long

                        for (choice in cmdOption.choices) {
                            choice(choice.name, choice.value as Long, choice.nameLocalizations.optional())
                        }
                    }
                } else error("The $cmdOption should be choiceable, but it isn't! Bug?")
            is NumberCommandOption, is NullableNumberCommandOption ->
                if (cmdOption is ChoiceableCommandOption<*, *>) {
                    builder.number(cmdOption.name, cmdOption.description) {
                        this.nameLocalizations = cmdOption.nameLocalizations?.toMutableMap()
                        this.descriptionLocalizations = cmdOption.descriptionLocalizations?.toMutableMap()
                        this.required = cmdOption !is NullableCommandOption
                        this.autocomplete = cmdOption.autoCompleteExecutorDeclaration != null

                        val numericOption = (cmdOption as NumericCommandOption<*, *>)
                        this.minValue = numericOption.minValue as Double
                        this.maxValue = numericOption.maxValue as Double

                        for (choice in cmdOption.choices) {
                            choice(choice.name, choice.value as Double, choice.nameLocalizations.optional())
                        }
                    }
                } else error("The $cmdOption should be choiceable, but it isn't! Bug?")
            is StringCommandOption, is NullableStringCommandOption ->
                if (cmdOption is ChoiceableCommandOption<*, *>) {
                    builder.string(cmdOption.name, cmdOption.description) {
                        this.nameLocalizations = cmdOption.nameLocalizations?.toMutableMap()
                        this.descriptionLocalizations = cmdOption.descriptionLocalizations?.toMutableMap()
                        this.required = cmdOption !is NullableCommandOption
                        this.autocomplete = cmdOption.autoCompleteExecutorDeclaration != null

                        for (choice in cmdOption.choices) {
                            choice(choice.name, choice.value as String, choice.nameLocalizations.optional())
                        }
                    }
                } else error("The $cmdOption should be choiceable, but it isn't! Bug?")
            is BooleanCommandOption, is NullableBooleanCommandOption ->
                builder.boolean(cmdOption.name, cmdOption.description) {
                    this.nameLocalizations = cmdOption.nameLocalizations?.toMutableMap()
                    this.descriptionLocalizations = cmdOption.descriptionLocalizations?.toMutableMap()
                    this.required = cmdOption !is NullableCommandOption
                }
            is UserCommandOption, is NullableUserCommandOption ->
                builder.user(cmdOption.name, cmdOption.description) {
                    this.nameLocalizations = cmdOption.nameLocalizations?.toMutableMap()
                    this.descriptionLocalizations = cmdOption.descriptionLocalizations?.toMutableMap()
                    this.required = cmdOption !is NullableCommandOption
                }
            is ChannelCommandOption, is NullableChannelCommandOption ->
                builder.channel(cmdOption.name, cmdOption.description) {
                    this.nameLocalizations = cmdOption.nameLocalizations?.toMutableMap()
                    this.descriptionLocalizations = cmdOption.descriptionLocalizations?.toMutableMap()
                    this.required = cmdOption !is NullableCommandOption
                }
            is RoleCommandOption, is NullableRoleCommandOption ->
                builder.role(cmdOption.name, cmdOption.description) {
                    this.nameLocalizations = cmdOption.nameLocalizations?.toMutableMap()
                    this.descriptionLocalizations = cmdOption.descriptionLocalizations?.toMutableMap()
                    this.required = cmdOption !is NullableCommandOption
                }
            is AttachmentCommandOption, is NullableAttachmentCommandOption ->
                builder.attachment(cmdOption.name, cmdOption.description) {
                    this.nameLocalizations = cmdOption.nameLocalizations?.toMutableMap()
                    this.descriptionLocalizations = cmdOption.descriptionLocalizations?.toMutableMap()
                    this.required = cmdOption !is NullableCommandOption
                }
            else -> error("Unsupported type ${cmdOption::class}")
        }
    }
}