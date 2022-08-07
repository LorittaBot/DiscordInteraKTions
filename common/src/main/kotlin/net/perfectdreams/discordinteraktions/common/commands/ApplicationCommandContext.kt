package net.perfectdreams.discordinteraktions.common.commands

import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.User
import net.perfectdreams.discordinteraktions.common.InteractionContext
import net.perfectdreams.discordinteraktions.common.requests.RequestBridge
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData

open class ApplicationCommandContext(
    bridge: RequestBridge,
    sender: User,
    channelId: Snowflake,
    data: InteractionData,
    discordInteractionData: DiscordInteraction,
    val applicationCommandDeclaration: ApplicationCommandDeclaration
) : InteractionContext(bridge, sender, channelId, data, discordInteractionData)