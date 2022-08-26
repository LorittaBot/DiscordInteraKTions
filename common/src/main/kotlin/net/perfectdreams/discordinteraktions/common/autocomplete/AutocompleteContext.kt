package net.perfectdreams.discordinteraktions.common.autocomplete

import dev.kord.common.entity.CommandArgument
import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.User
import net.perfectdreams.discordinteraktions.common.commands.options.DiscordCommandOption
import net.perfectdreams.discordinteraktions.common.commands.options.OptionReference
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData

// This doesn't inherit from InteractionContext because we can't send messages on a autocomplete request
open class AutocompleteContext(
    val sender: User,
    val channelId: Snowflake,
    val data: InteractionData,
    val arguments: List<CommandArgument<*>>,

    /**
     * The interaction data object from Discord, useful if you need to use data that is not exposed directly via Discord InteraKTions
     */
    val discordInteraction: DiscordInteraction
) {
    /**
     * Gets the filled argument from the [arguments] map, matching it via the [DiscordCommandOption].
     *
     * The reason the return value is [T?] is that a user can skip previous options and go straight to the autocompletable option, which
     * in that case, the option will be null because it wasn't filled by the user.
     *
     * **You should not use this to match the focused option!** Due to the way Discord works, a [CommandArgument.AutoCompleteArgument] is a String, so
     * it would be impossible to return the data in the proper type to you. If you do this, an error will be thrown.
     *
     * @param option the command option
     */
    fun <T> getArgument(option: OptionReference<T>): T? {
        val matchedArgument = arguments.firstOrNull { it.name == option.name }

        if (matchedArgument is CommandArgument.AutoCompleteArgument)
            error("If you want to get the focused argument, please use the \"focusedOption\" argument")

        return (matchedArgument as? CommandArgument<T>)?.value
    }
}