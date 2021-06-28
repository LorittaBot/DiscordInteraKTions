package net.perfectdreams.discordinteraktions.platform.jda.context.manager

import net.dv8tion.jda.api.interactions.components.ButtonInteraction
import net.perfectdreams.discordinteraktions.common.context.RequestBridge
import net.perfectdreams.discordinteraktions.common.utils.ActionRowComponent
import net.perfectdreams.discordinteraktions.common.utils.ButtonComponent
import net.perfectdreams.discordinteraktions.common.utils.InteractionMessage
import net.perfectdreams.discordinteraktions.platform.jda.utils.JDAConversionUtils
import net.perfectdreams.discordinteraktions.platform.jda.utils.await

class JDAButtonRequestManager(bridge: RequestBridge, private val interaction: ButtonInteraction) : JDARequestManager(bridge, interaction) {
    override suspend fun deferEdit(message: InteractionMessage?) {
        if (message != null) {
            interaction.deferEdit()
                .setContent(message.content)
                .await()
        }
    }
}