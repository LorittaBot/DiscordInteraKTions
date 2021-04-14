package net.perfectdreams.discordinteraktions.commands

import net.perfectdreams.discordinteraktions.context.SlashCommandContext
import net.perfectdreams.discordinteraktions.declarations.slash.SlashCommandDeclaration

/**
 * This is the class that should be inherited if you
 * want to create a Slash Command.
 */
abstract class SlashCommandExecutor {
    abstract suspend fun execute(context: SlashCommandContext, args: SlashCommandArguments)
}