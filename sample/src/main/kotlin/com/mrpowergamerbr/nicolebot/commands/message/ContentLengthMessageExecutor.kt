package com.mrpowergamerbr.nicolebot.commands.message

import net.perfectdreams.discordinteraktions.common.commands.ApplicationCommandContext
import net.perfectdreams.discordinteraktions.common.commands.MessageCommandExecutor
import net.perfectdreams.discordinteraktions.common.entities.messages.Message

class ContentLengthMessageExecutor : MessageCommandExecutor() {
    override suspend fun execute(context: ApplicationCommandContext, targetMessage: Message) {
        context.sendEphemeralMessage {
            content = "The message has ${targetMessage.content?.length} characters!"
        }
    }
}