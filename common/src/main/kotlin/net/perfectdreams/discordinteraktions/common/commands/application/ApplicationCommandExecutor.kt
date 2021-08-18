package net.perfectdreams.discordinteraktions.common.commands.application

import net.perfectdreams.discordinteraktions.common.commands.interaction.InteractionCommandExecutor
import net.perfectdreams.discordinteraktions.common.context.commands.ApplicationCommandContext

/**
 * This is the class that should be inherited if you
 * want to create an Application Command.
 */
abstract class ApplicationCommandExecutor : InteractionCommandExecutor() {
    abstract suspend fun execute(context: ApplicationCommandContext)
}