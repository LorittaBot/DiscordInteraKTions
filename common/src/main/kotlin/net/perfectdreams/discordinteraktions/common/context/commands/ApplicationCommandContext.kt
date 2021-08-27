package net.perfectdreams.discordinteraktions.common.context.commands

import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.common.context.InteractionContext
import net.perfectdreams.discordinteraktions.common.context.RequestBridge
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData

open class ApplicationCommandContext(
    bridge: RequestBridge,
    sender: User,
    data: InteractionData
) : InteractionContext(bridge, sender, data) {

}