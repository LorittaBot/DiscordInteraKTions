package net.perfectdreams.discordinteraktions.commands

import net.perfectdreams.discordinteraktions.context.SlashCommandContext
import net.perfectdreams.discordinteraktions.declarations.slash.SlashCommandDeclaration

/**
 * This is the class that should be inherited if you
 * want to create a Slash Command.
 *
 * It's recommended that the [declaration] parameter be
 * the class' companion object that extends [SlashCommandDeclaration].
 */
abstract class SlashCommand(val declaration: SlashCommandDeclaration) {

    /**
     * This is the method that'll be called when this command
     * is executed, even if some of the options're not matched (you should handle this kind of error)
     *
     * @param context The context including the command executor, the channel, guild, etc...
     */
    abstract suspend fun executes(context: SlashCommandContext)
}