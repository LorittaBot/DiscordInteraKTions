package net.perfectdreams.discordinteraktions.common.commands

import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.Snowflake
import net.perfectdreams.discordinteraktions.common.entities.InteractionMember
import net.perfectdreams.discordinteraktions.common.entities.User
import net.perfectdreams.discordinteraktions.common.requests.RequestBridge
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData

open class GuildApplicationCommandContext(
    bridge: RequestBridge,
    sender: User,
    channelId: Snowflake,
    data: InteractionData,
    discordInteractionData: DiscordInteraction,
    applicationCommandDeclaration: ApplicationCommandDeclaration,
    val guildId: Snowflake,
    val member: InteractionMember
) : ApplicationCommandContext(bridge, sender, channelId, data, discordInteractionData, applicationCommandDeclaration) {
    val appPermissions = discordInteractionData.appPermissions.value ?: error("App Permissions field is null on a Guild Interaction! Bug?")
}