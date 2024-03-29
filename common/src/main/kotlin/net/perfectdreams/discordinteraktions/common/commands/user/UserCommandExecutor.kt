package net.perfectdreams.discordinteraktions.common.commands.user

import net.perfectdreams.discordinteraktions.api.entities.Member
import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.common.commands.interaction.InteractionCommandExecutor
import net.perfectdreams.discordinteraktions.common.context.commands.ApplicationCommandContext

/**
 * This is the class that should be inherited if you
 * want to create an User Command.
 */
abstract class UserCommandExecutor : InteractionCommandExecutor() {
    abstract suspend fun execute(context: ApplicationCommandContext, targetUser: User, targetMember: Member?)
}