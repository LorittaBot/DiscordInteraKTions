package net.perfectdreams.discordinteraktions.common

import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.Snowflake
import net.perfectdreams.discordinteraktions.common.entities.User
import net.perfectdreams.discordinteraktions.common.requests.RequestBridge
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData

open class InteractionContext(
    bridge: RequestBridge,
    val sender: User,
    val channelId: Snowflake,
    val interactionData: InteractionData,

    /**
     * The interaction data object from Discord, useful if you need to use data that is not exposed directly via Discord InteraKTions
     */
    val discordInteraction: DiscordInteraction
) : BarebonesInteractionContext(bridge)