package com.mrpowergamerbr.nicolebot.commands.declarations

import com.mrpowergamerbr.nicolebot.commands.SendYourAttachmentExecutor
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandDeclarationWrapper
import net.perfectdreams.discordinteraktions.common.commands.slashCommand

object SendYourAttachmentCommand : SlashCommandDeclarationWrapper {
    override fun declaration() = slashCommand("sendyourattachment", "Send your Attachment!") {
        executor = SendYourAttachmentExecutor
    }
}