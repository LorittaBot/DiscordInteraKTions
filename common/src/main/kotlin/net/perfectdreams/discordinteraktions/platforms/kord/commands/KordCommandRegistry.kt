package net.perfectdreams.discordinteraktions.platforms.kord.commands

import dev.kord.common.entity.Snowflake
import dev.kord.common.entity.optional.optional
import dev.kord.rest.builder.interaction.*
import dev.kord.rest.service.RestClient
import net.perfectdreams.discordinteraktions.common.commands.*
import net.perfectdreams.discordinteraktions.common.commands.options.*

class KordCommandRegistry(
    private val applicationId: Snowflake,
    private val rest: RestClient,
    private val manager: CommandManager
) : CommandRegistry {
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

    private fun convertCommandDeclarationToKord(
        builder: MultiApplicationCommandBuilder,
        declaration: ApplicationCommandDeclaration
    ) {
        // Workaround because Kord's CommandCreateBuilder builders are internal now
        when (declaration) {
            is UserCommandDeclaration -> {
                return builder.user(declaration.name) {
                    nameLocalizations = declaration.nameLocalizations?.toMutableMap()
                    defaultMemberPermissions = declaration.defaultMemberPermissions

                    if (builder is GlobalMultiApplicationCommandBuilder)
                        (this as GlobalUserCommandCreateBuilder).dmPermission = declaration.dmPermission
                }
            }

            is MessageCommandDeclaration -> {
                return builder.message(declaration.name) {
                    nameLocalizations = declaration.nameLocalizations?.toMutableMap()
                    defaultMemberPermissions = declaration.defaultMemberPermissions

                    if (builder is GlobalMultiApplicationCommandBuilder)
                        (this as GlobalMessageCommandCreateBuilder).dmPermission = declaration.dmPermission
                }
            }

            is SlashCommandDeclaration -> {
                builder.input(declaration.name, declaration.description) {
                    nameLocalizations = declaration.nameLocalizations?.toMutableMap()
                    descriptionLocalizations = declaration.descriptionLocalizations?.toMutableMap()
                    defaultMemberPermissions = declaration.defaultMemberPermissions

                    if (builder is GlobalMultiApplicationCommandBuilder)
                        (this as GlobalChatInputCreateBuilder).dmPermission = declaration.dmPermission
                    
                    options = mutableListOf() // Initialize an empty list so we can use it

                    // We can only have (subcommands OR subcommand groups) OR arguments
                    if (declaration.subcommands.isNotEmpty() || declaration.subcommandGroups.isNotEmpty()) {
                        declaration.subcommands.forEach {
                            options?.add(convertSubcommandDeclarationToKord(it))
                        }

                        declaration.subcommandGroups.forEach {
                            options?.add(convertSubcommandGroupDeclarationToKord(it))
                        }
                    } else {
                        require(declaration.executor != null) { "Root command without a executor!" }

                        declaration.options?.forEach {
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

        // This is a subcommand, so we only have an executor anyway
        require(declaration.executor != null) { "Subcommand without a executor!" }

        declaration.options?.forEach {
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

    private fun convertCommandOptionToKord(cmdOption: InteraKTionsCommandOption<*>, builder: BaseInputChatBuilder) {
        cmdOption.register(builder)
    }
}