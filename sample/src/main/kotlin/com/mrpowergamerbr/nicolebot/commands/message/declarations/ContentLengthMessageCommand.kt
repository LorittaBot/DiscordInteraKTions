package com.mrpowergamerbr.nicolebot.commands.message.declarations

import com.mrpowergamerbr.nicolebot.commands.message.ContentLengthMessageExecutor
import com.mrpowergamerbr.nicolebot.commands.user.ViewAvatarUserExecutor
import net.perfectdreams.discordinteraktions.common.commands.MessageCommandDeclarationWrapper
import net.perfectdreams.discordinteraktions.common.commands.UserCommandDeclarationWrapper
import net.perfectdreams.discordinteraktions.common.commands.messageCommand
import net.perfectdreams.discordinteraktions.common.commands.userCommand

object ContentLengthMessageCommand : MessageCommandDeclarationWrapper {
    override fun declaration() = messageCommand("View content length", ContentLengthMessageExecutor())
}