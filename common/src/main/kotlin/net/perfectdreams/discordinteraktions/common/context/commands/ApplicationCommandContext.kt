package net.perfectdreams.discordinteraktions.common.context.commands

import dev.kord.common.entity.Snowflake
import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.common.context.InteractionContext
import net.perfectdreams.discordinteraktions.common.context.RequestBridge
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData

open class ApplicationCommandContext(
    bridge: RequestBridge,
    sender: User,
    channelId: Snowflake,
    data: InteractionData
) : InteractionContext(bridge, sender, channelId, data) {

}