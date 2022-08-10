package net.perfectdreams.discordinteraktions.common

import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.rest.builder.interaction.*
import net.perfectdreams.discordinteraktions.common.commands.*
import net.perfectdreams.discordinteraktions.common.commands.options.InteraKTionsCommandOption
import net.perfectdreams.discordinteraktions.platforms.kord.utils.KordAutocompleteChecker
import net.perfectdreams.discordinteraktions.platforms.kord.utils.KordCommandChecker
import net.perfectdreams.discordinteraktions.platforms.kord.utils.KordComponentChecker
import net.perfectdreams.discordinteraktions.platforms.kord.utils.KordModalChecker

class DiscordInteraKTions(
    val kord: Kord,
    val applicationId: Snowflake,
) {
    val rest = kord.rest
    val manager = InteractionsManager()

    val autocompleteChecker = KordAutocompleteChecker(kord, manager)
    val commandChecker = KordCommandChecker(kord, manager)
    val componentChecker = KordComponentChecker(kord, manager)
    val modalChecker = KordModalChecker(kord, manager)

    /**
     * Upserts all commands in the guild [guildId]
     */
    suspend fun updateAllCommandsInGuild(guildId: Snowflake) {
        rest.interaction.createGuildApplicationCommands(
            applicationId,
            guildId,
            createGuildApplicationCommandCreateRequests()
        )
    }

    /**
     * Upserts all global commands
     */
    suspend fun updateAllGlobalCommands() {
        val kordApplicationId = applicationId

        rest.interaction.createGlobalApplicationCommands(
            kordApplicationId,
            createGlobalApplicationCommandCreateRequests()
        )
    }

    /**
     * Creates a list of guild [ApplicationCommandCreateRequest], which can be used with Kord to register the command on Discord.
     *
     * Because [ApplicationCommandCreateRequest] is [Serializable], you can create a hash of the requests and track if your bot needs to send an upsert request do Discord or not,
     * which can be useful to avoid ratelimits.
     */
    fun createGuildApplicationCommandCreateRequests() = GuildMultiApplicationCommandBuilder().apply {
        manager.applicationCommandsDeclarations.forEach {
            convertCommandDeclarationToKord(this, it)
        }
    }.build()

    /**
     * Creates a list of global [ApplicationCommandCreateRequest], which can be used with Kord to register the command on Discord.
     *
     * Because [ApplicationCommandCreateRequest] is [Serializable], you can create a hash of the requests and track if your bot needs to send an upsert request do Discord or not,
     * which can be useful to avoid ratelimits.
     */
    fun createGlobalApplicationCommandCreateRequests() = GlobalMultiApplicationCommandBuilder().apply {
        manager.applicationCommandsDeclarations.forEach {
            convertCommandDeclarationToKord(this, it)
        }
    }.build()

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
                        val executor = declaration.executor

                        require(executor != null) { "Root command without a executor!" }

                        executor.options.registeredOptions.forEach {
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

        // This is a subcommand, so we SHOUlD have an non-null executor
        val executor = declaration.executor

        require(executor != null) { "Subcommand without a executor!" }

        executor.options.registeredOptions.forEach {
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

fun DiscordInteraKTions(token: String, applicationId: Snowflake) = DiscordInteraKTions(
    Kord.restOnly(token),
    applicationId
)