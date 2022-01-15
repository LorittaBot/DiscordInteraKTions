package net.perfectdreams.discordinteraktions.common.commands

import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.Snowflake
import net.perfectdreams.discordinteraktions.common.entities.Member
import net.perfectdreams.discordinteraktions.common.entities.User
import net.perfectdreams.discordinteraktions.common.requests.RequestBridge
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData

open class GuildApplicationCommandContext(
    bridge: RequestBridge,
    sender: User,
    channelId: Snowflake,
    data: InteractionData,
    discordInteractionData: DiscordInteraction,
    val guildId: Snowflake,
    val member: Member
) : ApplicationCommandContext(bridge, sender, channelId, data, discordInteractionData)