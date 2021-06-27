package net.perfectdreams.discordinteraktions.platform.jda.context.manager

import net.dv8tion.jda.api.interactions.InteractionHook
import net.perfectdreams.discordinteraktions.common.context.RequestBridge
import net.perfectdreams.discordinteraktions.common.context.RequestManager
import net.perfectdreams.discordinteraktions.common.entities.DummyMessage
import net.perfectdreams.discordinteraktions.common.entities.Message
import net.perfectdreams.discordinteraktions.common.utils.InteractionMessage
import net.perfectdreams.discordinteraktions.platform.jda.utils.await

class JDAHookRequestManager(bridge: RequestBridge, private val hook: InteractionHook) : RequestManager(bridge) {
    override suspend fun defer(isEphemeral: Boolean) = throw UnsupportedOperationException("Can't defer a interaction that was already deferred!")

    override suspend fun sendMessage(message: InteractionMessage): Message {
        hook.sendMessage(message.content)
            .setEphemeral(message.isEphemeral == true) // TODO: Fix this
            .also {
                message.files?.forEach { (fileName, content) ->
                    it.addFile(content, fileName)
                }
            }
            .await()
        return DummyMessage()
    }
}