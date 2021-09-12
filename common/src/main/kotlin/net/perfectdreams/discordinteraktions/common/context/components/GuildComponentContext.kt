package net.perfectdreams.discordinteraktions.common.context.components

import dev.kord.common.entity.Snowflake
import net.perfectdreams.discordinteraktions.api.entities.Member
import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.common.context.RequestBridge
import net.perfectdreams.discordinteraktions.common.entities.messages.Message
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData

open class GuildComponentContext(
    bridge: RequestBridge,
    sender: User,
    message: Message,
    data: InteractionData,
    val guildId: Snowflake,
    val member: Member
) : ComponentContext(bridge, sender, message, data)