package net.perfectdreams.discordinteraktions.utils

import dev.kord.common.entity.CommandGroup
import dev.kord.common.entity.Option
import dev.kord.common.entity.SubCommand
import net.perfectdreams.discordinteraktions.declarations.slash.SlashCommandDeclaration
import net.perfectdreams.discordinteraktions.entities.CommandInteraction

object CommandDeclarationUtils {
    /**
     * Finds all command declaration names in the [request]
     *
     * If a command only has one single command (example: `/loritta`) then the list will only have a single [CommandLabel]
     *
     * If a command has multiple subcommands and subgroups (example: `/loritta morenitta/cute`, where `morenitta/cute` is two subcommands) then the list
     * will have a [CommandLabel] and a [SubCommandLabel]
     *
     * @see areLabelsConnectedToCommandDeclaration
     *
     * @param request the command interaction
     * @return a list with all of the labels
     */
    fun findAllSubcommandDeclarationNames(request: CommandInteraction): List<CommandLabel> {
        val commandLabels = mutableListOf(CommandLabel(request.data.name))
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
     * @param rootDeclaration the root declaration
     * @param declaration     the declaration that must be found
     * @return if the labels are connected or not
     */
    fun areLabelsConnectedToCommandDeclaration(labels: List<CommandLabel>, rootDeclaration: SlashCommandDeclaration, declaration: SlashCommandDeclaration): Boolean {
        println("Labels: ${labels.size} (${labels.joinToString { it.label }}); Root Declaration: $rootDeclaration; Declaration: $declaration")

        // If there aren't any more labels...
        val label = labels.firstOrNull() ?: run {
            // Then if the rootDeclaration is our declaration, then it means that we found it!
            // If it isn't, then F
            return rootDeclaration == declaration
        }

        println("Checking ${label} - ${label.label}")

        return when (label) {
            is SubCommandLabel -> {
                println("Matched with Sub Command Label!")
                if (rootDeclaration.name == label.label) {
                    areLabelsConnectedToCommandDeclaration(
                        labels.drop(1),
                        rootDeclaration,
                        declaration
                    )
                } else {
                    val subCommandOption =
                        rootDeclaration.options.subcommands.firstOrNull { it.name == label.label } ?: return false

                    if (subCommandOption.name == label.label) {
                        areLabelsConnectedToCommandDeclaration(
                            labels.drop(1),
                            subCommandOption,
                            declaration
                        )
                    } else false
                }
            }

            is CommandGroupLabel -> {
                println("Matched with Command Group Label!")
                val subCommandOption = rootDeclaration.options.subcommandGroups.firstOrNull { it.name == label.label } ?: return false

                if (subCommandOption.name == label.label) {
                    subCommandOption.subcommands.any {
                        areLabelsConnectedToCommandDeclaration(
                            labels.drop(1),
                            it,
                            declaration
                        )
                    }
                } else false
            }

            // This is the default "CommandLabel"
            else -> {
                println("Matched with Default Label!")
                if (rootDeclaration.name == label.label)
                    areLabelsConnectedToCommandDeclaration(
                        labels.drop(1),
                        rootDeclaration,
                        declaration
                    )
                else false
            }
        }
    }

    open class CommandLabel(val label: String)
    class SubCommandLabel(label: String) : CommandLabel(label)
    class CommandGroupLabel(label: String) : CommandLabel(label)
}