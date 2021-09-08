package net.perfectdreams.discordinteraktions.common.context.buttons

import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.common.context.RequestBridge
import net.perfectdreams.discordinteraktions.common.context.components.ComponentContext
import net.perfectdreams.discordinteraktions.common.entities.messages.Message
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData

open class ButtonClickContext(
    bridge: RequestBridge,
    sender: User,
    message: Message,
    data: InteractionData
) : ComponentContext(bridge, sender, message, data)