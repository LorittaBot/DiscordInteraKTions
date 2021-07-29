package net.perfectdreams.discordinteraktions.common.context.commands

import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.common.context.InteractionContext
import net.perfectdreams.discordinteraktions.common.context.InteractionRequestState
import net.perfectdreams.discordinteraktions.common.context.RequestBridge
import net.perfectdreams.discordinteraktions.common.entities.Message
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData
import net.perfectdreams.discordinteraktions.common.utils.InteractionMessage
import net.perfectdreams.discordinteraktions.common.utils.MessageBuilder
import net.perfectdreams.discordinteraktions.common.utils.buildMessage

open class SlashCommandContext(
    bridge: RequestBridge,
    sender: User,
    data: InteractionData
) : InteractionContext(bridge, sender, data)