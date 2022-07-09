package net.perfectdreams.discordinteraktions.common.components

import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.Snowflake
import net.perfectdreams.discordinteraktions.common.entities.InteractionMember
import net.perfectdreams.discordinteraktions.common.entities.User
import net.perfectdreams.discordinteraktions.common.requests.RequestBridge
import net.perfectdreams.discordinteraktions.common.entities.messages.Message
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData

open class GuildComponentContext(
    bridge: RequestBridge,
    sender: User,
    channelId: Snowflake,
    message: Message,
    data: InteractionData,
    discordInteractionData: DiscordInteraction,
    val guildId: Snowflake,
    val member: InteractionMember
) : ComponentContext(bridge, sender, channelId, message, data, discordInteractionData) {
    val appPermissions = discordInteractionData.appPermissions.value ?: error("App Permissions field is null on a Guild Interaction! Bug?")
}