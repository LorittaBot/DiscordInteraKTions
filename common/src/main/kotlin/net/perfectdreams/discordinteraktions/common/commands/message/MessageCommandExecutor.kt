package net.perfectdreams.discordinteraktions.common.commands.message

import net.perfectdreams.discordinteraktions.common.commands.interaction.InteractionCommandExecutor
import net.perfectdreams.discordinteraktions.common.context.commands.ApplicationCommandContext
import net.perfectdreams.discordinteraktions.common.entities.messages.Message

/**
 * This is the class that should be inherited if you
 * want to create an Message Command.
 */
abstract class MessageCommandExecutor : InteractionCommandExecutor() {
    abstract suspend fun execute(context: ApplicationCommandContext, targetMessage: Message)
}