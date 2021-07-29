package net.perfectdreams.discordinteraktions.common.context.commands

import net.perfectdreams.discordinteraktions.api.entities.Guild
import net.perfectdreams.discordinteraktions.api.entities.Member
import net.perfectdreams.discordinteraktions.api.entities.Snowflake
import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.common.context.RequestBridge
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData

open class GuildSlashCommandContext(
    bridge: RequestBridge,
    sender: User,
    data: InteractionData,
    val guildId: Snowflake,
    val member: Member
) : SlashCommandContext(bridge, sender, data)