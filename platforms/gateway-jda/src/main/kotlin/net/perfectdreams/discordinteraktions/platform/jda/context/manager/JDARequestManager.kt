package net.perfectdreams.discordinteraktions.platform.jda.context.manager

import net.dv8tion.jda.api.interactions.Interaction
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.Button
import net.dv8tion.jda.api.interactions.components.ButtonStyle
import net.perfectdreams.discordinteraktions.common.context.InteractionRequestState
import net.perfectdreams.discordinteraktions.common.context.RequestBridge
import net.perfectdreams.discordinteraktions.common.context.RequestManager
import net.perfectdreams.discordinteraktions.common.entities.DummyMessage
import net.perfectdreams.discordinteraktions.common.entities.Message
import net.perfectdreams.discordinteraktions.common.utils.ActionRowComponent
import net.perfectdreams.discordinteraktions.common.utils.ButtonComponent
import net.perfectdreams.discordinteraktions.common.utils.InteractionMessage
import net.perfectdreams.discordinteraktions.platform.jda.utils.JDAConversionUtils
import net.perfectdreams.discordinteraktions.platform.jda.utils.await

class JDARequestManager(bridge: RequestBridge, private val interaction: Interaction) : RequestManager(bridge) {
    override suspend fun defer(isEphemeral: Boolean) {
        val hook = interaction
            .deferReply(isEphemeral)
            .await()

        bridge.manager = JDAHookRequestManager(
            bridge,
            hook
        )

        bridge.state.value = InteractionRequestState.DEFERRED
    }

    override suspend fun sendMessage(message: InteractionMessage): Message {
        val hook = interaction
            .reply(message.content)
            .setEphemeral(
                message.isEphemeral == true
            ) // TODO: Fix this, it works but it is kinda wonky
            .also {
                message.files?.forEach { (fileName, content) ->
                    it.addFile(content, fileName)
                }

                for (component in message.components) {
                    if (component is ButtonComponent)
                        it.addActionRow(JDAConversionUtils.convertButtonComponent(component))
                    else if (component is ActionRowComponent)
                        it.addActionRows(JDAConversionUtils.convertActionRowComponent(component))
                }
            }
            .await()

        bridge.manager = JDAHookRequestManager(
            bridge,
            hook
        )

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED

        return DummyMessage()
    }
}