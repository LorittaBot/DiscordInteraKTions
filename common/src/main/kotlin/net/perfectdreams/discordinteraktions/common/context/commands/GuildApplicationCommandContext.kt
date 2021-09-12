package net.perfectdreams.discordinteraktions.common.context.commands

import dev.kord.common.entity.Snowflake
import net.perfectdreams.discordinteraktions.api.entities.Member
import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.common.context.RequestBridge
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData

open class GuildApplicationCommandContext(
    bridge: RequestBridge,
    sender: User,
    data: InteractionData,
    val guildId: Snowflake,
    val member: Member
) : ApplicationCommandContext(bridge, sender, data)