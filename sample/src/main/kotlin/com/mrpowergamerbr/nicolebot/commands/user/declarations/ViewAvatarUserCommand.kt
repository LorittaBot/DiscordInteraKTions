package com.mrpowergamerbr.nicolebot.commands.user.declarations

import com.mrpowergamerbr.nicolebot.commands.user.ViewAvatarUserExecutor
import net.perfectdreams.discordinteraktions.common.commands.UserCommandDeclarationWrapper
import net.perfectdreams.discordinteraktions.common.commands.userCommand

object ViewAvatarUserCommand : UserCommandDeclarationWrapper {
    override fun declaration() = userCommand("View avatar", ViewAvatarUserExecutor())
}