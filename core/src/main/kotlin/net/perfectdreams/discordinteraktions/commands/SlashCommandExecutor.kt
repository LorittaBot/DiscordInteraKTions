package net.perfectdreams.discordinteraktions.commands

import net.perfectdreams.discordinteraktions.context.SlashCommandContext
import net.perfectdreams.discordinteraktions.declarations.slash.SlashCommandDeclaration

/**
 * This is the class that should be inherited if you
 * want to create a Slash Command.
 */
abstract class SlashCommandExecutor {
    abstract suspend fun execute(context: SlashCommandContext, args: SlashCommandArguments)

    /**
     * Used by the [net.perfectdreams.discordinteraktions.declarations.slash.SlashCommandExecutorDeclaration] to match declarations to executors.
     *
     * By default the class of the executor is used, but this may cause issues when using anonymous classes!
     *
     * To avoid this issue, you can replace the signature with another unique identifier
     */
    open fun signature(): Any = this::class
}