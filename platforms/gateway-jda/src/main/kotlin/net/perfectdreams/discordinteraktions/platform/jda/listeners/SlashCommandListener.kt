package net.perfectdreams.discordinteraktions.platform.jda.listeners

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.events.RawGatewayEvent
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.perfectdreams.discordinteraktions.api.entities.Snowflake
import net.perfectdreams.discordinteraktions.common.buttons.ButtonStateManager
import net.perfectdreams.discordinteraktions.common.commands.CommandManager
import net.perfectdreams.discordinteraktions.common.context.commands.GuildSlashCommandContext
import net.perfectdreams.discordinteraktions.common.context.InteractionRequestState
import net.perfectdreams.discordinteraktions.common.context.RequestBridge
import net.perfectdreams.discordinteraktions.common.context.buttons.ButtonClickContext
import net.perfectdreams.discordinteraktions.common.context.commands.SlashCommandArguments
import net.perfectdreams.discordinteraktions.common.context.commands.SlashCommandContext
import net.perfectdreams.discordinteraktions.common.utils.Observable
import net.perfectdreams.discordinteraktions.declarations.slash.SlashCommandDeclarationBuilder
import net.perfectdreams.discordinteraktions.declarations.slash.options.CommandOption
import net.perfectdreams.discordinteraktions.declarations.slash.options.CommandOptionType
import net.perfectdreams.discordinteraktions.platform.jda.context.manager.JDAButtonRequestManager
import net.perfectdreams.discordinteraktions.platform.jda.context.manager.JDARequestManager
import net.perfectdreams.discordinteraktions.platform.jda.entities.JDAGuild
import net.perfectdreams.discordinteraktions.platform.jda.entities.JDAMember
import net.perfectdreams.discordinteraktions.platform.jda.entities.JDAUser
import java.util.*

class SlashCommandListener(private val manager: CommandManager) : ListenerAdapter() {
    /* override fun onRawGateway(event: RawGatewayEvent) {
        println(event.type)
        println(event.payload)
    }

    override fun onButtonClick(event: ButtonClickEvent) {
        val bridge = RequestBridge(Observable(InteractionRequestState.NOT_REPLIED_YET))
        val requestManager = JDAButtonRequestManager(bridge, event.interaction)
        bridge.manager = requestManager

        GlobalScope.launch {
            val data = buttonStateManager.getStateById(UUID.fromString(event.componentId))

            val executor = buttonStateManager.buttonExecutors.first {
                it.signature() == data.signature
            }

            executor.onClickConvertToProperData(
                JDAUser(event.user),
                ButtonClickContext(bridge, JDAUser(event.user)),
                data.data
            )
        }
    } */

    override fun onSlashCommand(event: SlashCommandEvent) {
        GlobalScope.launch {
            val bridge = RequestBridge(Observable(InteractionRequestState.NOT_REPLIED_YET))
            val requestManager = JDARequestManager(bridge, event.interaction)
            bridge.manager = requestManager

            val arguments = mutableMapOf<CommandOption<*>, Any?>()

            // Insert all the labels of the command to a list
            val labels = mutableListOf<CommandLabel>(
                RootCommandLabel(event.name)
            )

            event.subcommandGroup?.let {
                labels.add(CommandGroupLabel(it))
            }

            event.subcommandName?.let {
                labels.add(SubCommandLabel(it))
            }

            val command = manager.declarations
                .asSequence()
                .mapNotNull {
                    getLabelsConnectedToCommandDeclaration(
                        labels,
                        it
                    )
                }
                .first()

            val executorDeclaration = command.executor ?: return@launch
            val executor = manager.executors.first { it.signature() == executorDeclaration.parent }

            // Parse command options and bind them to Discord InteraKTions options
            executorDeclaration.options.arguments.forEach {
                val option = event.getOption(it.name)

                if (!it.type.isNullable && option == null)
                    error("Option is null but the type is required!")

                arguments[it] = when (it.type) {
                    is CommandOptionType.Integer, is CommandOptionType.NullableInteger -> option?.asLong?.toInt()
                    is CommandOptionType.String, is CommandOptionType.NullableString -> option?.asString
                    is CommandOptionType.Bool, is CommandOptionType.NullableBool -> option?.asBoolean
                    is CommandOptionType.User, is CommandOptionType.NullableUser -> option?.asUser?.let { JDAUser(it) }
                    else -> error("Unsupported type ${it.type}")
                }
            }

            val guild = event.guild
            val member = event.member

            executor
                .execute(
                    if (guild != null && member != null) {
                        GuildSlashCommandContext(bridge, JDAUser(event.user), Snowflake(guild.idLong), JDAMember(member))
                    } else {
                        SlashCommandContext(bridge, JDAUser(event.user))
                    },
                    SlashCommandArguments(arguments)
                )
        }
    }

    /**
     * Checks if the [labels] are connected from the [rootDeclaration] to the [declaration], by checking the [rootDeclaration] and its children until
     * the [declaration] is found.
     *
     * @param labels          the request labels in order
     * @param rootDeclaration the root declaration
     * @param declaration     the declaration that must be found
     * @return the matched declaration
     */
    fun getLabelsConnectedToCommandDeclaration(labels: List<CommandLabel>, declaration: SlashCommandDeclarationBuilder): SlashCommandDeclarationBuilder? {
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

    open class CommandLabel(val label: String)
    class RootCommandLabel(label: String) : CommandLabel(label)
    class SubCommandLabel(label: String) : CommandLabel(label)
    class CommandGroupLabel(label: String) : CommandLabel(label)
}