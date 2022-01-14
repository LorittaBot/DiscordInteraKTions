package net.perfectdreams.discordinteraktions.platforms.kord.commands

import dev.kord.common.entity.CommandArgument
import dev.kord.common.entity.CommandGroup
import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.Option
import dev.kord.common.entity.Snowflake
import dev.kord.common.entity.SubCommand
import mu.KotlinLogging
import net.perfectdreams.discordinteraktions.common.commands.CommandManager
import net.perfectdreams.discordinteraktions.declarations.commands.InteractionCommandDeclaration
import net.perfectdreams.discordinteraktions.declarations.commands.SlashCommandDeclaration
import net.perfectdreams.discordinteraktions.declarations.commands.slash.SlashCommandExecutorDeclaration
import net.perfectdreams.discordinteraktions.declarations.commands.slash.options.CommandOption
import net.perfectdreams.discordinteraktions.declarations.commands.slash.options.CommandOptionType
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordChannel
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordRole
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordUser

object CommandDeclarationUtils {
    private val logger = KotlinLogging.logger {}

    /**
     * Finds all command declaration names in the [request]
     *
     * If a command only has one single command (example: `/loritta`) then the list will only have a single [CommandLabel]
     *
     * If a command has multiple subcommands and subgroups (example: `/loritta morenitta/cute`, where `morenitta/cute` is two subcommands) then the list
     * will have a [CommandLabel] and a [SubCommandLabel]
     *
     * @see getLabelsConnectedToSlashCommandDeclaration
     *
     * @param request the command interaction
     * @return a list with all of the labels
     */
    fun findAllSubcommandDeclarationNames(request: DiscordInteraction): List<CommandLabel> {
        val commandLabels = mutableListOf<CommandLabel>(RootCommandLabel(request.data.name.value!!))
        val result = findAllSubcommandDeclarationNames(commandLabels, request.data.options.value)
        return result
    }

    private fun findAllSubcommandDeclarationNames(commandLabels: MutableList<CommandLabel>, options: List<Option>?): List<CommandLabel> {
        val firstOption = options?.firstOrNull()

        if (firstOption is SubCommand) {
            commandLabels.add(SubCommandLabel(firstOption.name))

            findAllSubcommandDeclarationNames(commandLabels, firstOption.options.value)
        } else if (firstOption is CommandGroup) {
            commandLabels.add(CommandGroupLabel(firstOption.name))

            findAllSubcommandDeclarationNames(commandLabels, firstOption.options.value)
        } else {
            return commandLabels
        }
        return commandLabels
    }

    /**
     * Gets the nested options in the [options]
     *
     * If the options are nested in `subcommand group` -> `subcommand` -> `command values`, this will return only the `command values`
     *
     * @param options the interaction options
     * @return the nested options
     */
    fun getNestedOptions(options: List<Option>?): List<Option>? {
        val firstOption = options?.firstOrNull()

        if (firstOption is SubCommand) {
            return getNestedOptions(firstOption.options.value)
        } else if (firstOption is CommandGroup) {
            return getNestedOptions(firstOption.options.value)
        }

        return options
    }

    /**
     * Checks if the [labels] are connected from the [rootDeclaration] to the [declaration], by checking the [rootDeclaration] and its children until
     * the [declaration] is found.
     *
     * @see findAllSubcommandDeclarationNames
     *
     * @param labels          the request labels in order
     * @param declaration     the declaration that must be found
     * @return the matched declaration
     */
    fun getLabelsConnectedToCommandDeclaration(labels: List<CommandLabel>, declaration: InteractionCommandDeclaration): InteractionCommandDeclaration? {
        if (declaration is SlashCommandDeclaration)
            return getLabelsConnectedToSlashCommandDeclaration(labels, declaration)

        // Let's not over complicate this, we already know that Discord only supports one level deep of nesting
        // (so group -> subcommand)
        // So let's do easy and quick checks
        if (labels.first() is RootCommandLabel && labels.first().label == declaration.name) {
            // Matches the root label! Yay!
            if (labels.size == 1)
            // If there is only a Root Label, then it means we found our root declaration!
                return declaration
        }
        return null
    }

    /**
     * Checks if the [labels] are connected from the [rootDeclaration] to the [declaration], by checking the [rootDeclaration] and its children until
     * the [declaration] is found.
     *
     * @see findAllSubcommandDeclarationNames
     *
     * @param labels          the request labels in order
     * @param declaration     the declaration that must be found
     * @return the matched declaration
     */
    private fun getLabelsConnectedToSlashCommandDeclaration(labels: List<CommandLabel>, declaration: SlashCommandDeclaration): SlashCommandDeclaration? {
        // Let's not over complicate this, we already know that Discord only supports one level deep of nesting
        // (so group -> subcommand)
        // So let's do easy and quick checks
        if (labels.first() is RootCommandLabel && labels.first().label == declaration.name) {
            // Matches the root label! Yay!
            if (labels.size == 1) {
                // If there is only a Root Label, then it means we found our root declaration!
                return declaration
            } else {
                val secondLabel = labels[1]

                // If not, let's check subcommand groups and subcommands
                // Thankfully we know when a label is a subcommand or a group!
                if (secondLabel is SubCommandLabel) {
                    for (subcommand in declaration.subcommands) {
                        if (secondLabel.label == subcommand.name) {
                            // Matches, then return this!
                            return subcommand
                        }
                    }
                    // Nothing found, return...
                    return null
                } else {
                    val thirdLabel = labels[2]

                    for (group in declaration.subcommandGroups) {
                        if (group.name == secondLabel.label) {
                            for (subcommand in group.subcommands) {
                                if (thirdLabel.label == subcommand.name) {
                                    // Matches, then return this!
                                    return subcommand
                                }
                            }
                        }
                    }
                    return null
                }
            }
        }
        return null
    }

    /**
     * Matches an application command declaration via the [commandLabels]
     *
     * @see getLabelsConnectedToSlashCommandDeclaration
     *
     * @param commandManager the command manager
     * @param commandLabels  the command labels
     * @return the matched declaration
     */
    inline fun <reified T : InteractionCommandDeclaration> getApplicationCommandDeclarationFromLabel(commandManager: CommandManager, commandLabels: List<CommandLabel>): T = commandManager.declarations
        .asSequence()
        .filterIsInstance<T>()
        .mapNotNull {
            getLabelsConnectedToCommandDeclaration(
                commandLabels,
                it
            )
        }
        .first() as T // I don't know why this cast is needed

    open class CommandLabel(val label: String)
    class RootCommandLabel(label: String) : CommandLabel(label)
    class SubCommandLabel(label: String) : CommandLabel(label)
    class CommandGroupLabel(label: String) : CommandLabel(label)

    fun convertOptions(request: DiscordInteraction, executorDeclaration: SlashCommandExecutorDeclaration, relativeOptions: List<Option>): Map<CommandOption<*>, Any?> {
        val arguments = mutableMapOf<CommandOption<*>, Any?>()

        for (option in relativeOptions) {
            val interaKTionOption = executorDeclaration.options.arguments
                .firstOrNull { it.name == option.name } ?: continue

            val argument = option as CommandArgument<*>

            arguments[interaKTionOption] = convertOption(
                interaKTionOption,
                argument,
                request
            )
        }

        return arguments
    }

    private fun convertOption(interaKTionOption: CommandOption<*>, argument: CommandArgument<*>, request: DiscordInteraction): Any? {
        logger.debug { interaKTionOption.type }
        logger.debug { argument.value }

        return when (interaKTionOption.type) {
            CommandOptionType.User, CommandOptionType.NullableUser -> {
                val userId = argument.value as Snowflake

                val resolved = request.data.resolved.value ?: return null
                val resolvedMap = resolved.users.value ?: return null
                val kordInstance = resolvedMap[userId] ?: return null

                // Now we need to wrap the kord user in our own implementation!
                return KordUser(kordInstance)
            }
            CommandOptionType.Channel, CommandOptionType.NullableChannel -> {
                val userId = argument.value as Snowflake

                val resolved = request.data.resolved.value ?: return null
                val resolvedMap = resolved.channels.value ?: return null
                val kordInstance = resolvedMap[userId] ?: return null

                // Now we need to wrap the kord user in our own implementation!
                return KordChannel(kordInstance)
            }
            CommandOptionType.Role, CommandOptionType.NullableRole -> {
                val userId = argument.value as Snowflake

                val resolved = request.data.resolved.value ?: return null
                val resolvedMap = resolved.roles.value ?: return null
                val kordInstance = resolvedMap[userId] ?: return null

                // Now we need to wrap the kord user in our own implementation!
                return KordRole(kordInstance)
            }
            else -> { argument.value }
        }
    }
}