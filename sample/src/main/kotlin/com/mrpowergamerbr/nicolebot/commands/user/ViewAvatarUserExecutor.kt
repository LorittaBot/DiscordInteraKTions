package com.mrpowergamerbr.nicolebot.commands.user

import net.perfectdreams.discordinteraktions.common.builder.message.embed
import net.perfectdreams.discordinteraktions.common.commands.ApplicationCommandContext
import net.perfectdreams.discordinteraktions.common.commands.UserCommandExecutor
import net.perfectdreams.discordinteraktions.common.entities.InteractionMember
import net.perfectdreams.discordinteraktions.common.entities.User

class ViewAvatarUserExecutor : UserCommandExecutor() {
    override suspend fun execute(
        context: ApplicationCommandContext,
        targetUser: User,
        targetMember: InteractionMember?
    ) {
        context.sendEphemeralMessage {
            embed {
                image = targetUser.avatar.cdnUrl.toUrl()
            }
        }
    }
}