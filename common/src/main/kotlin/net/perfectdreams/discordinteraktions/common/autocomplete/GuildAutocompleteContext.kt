package net.perfectdreams.discordinteraktions.common.autocomplete

import dev.kord.common.entity.CommandArgument
import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.Member
import dev.kord.core.entity.User
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData

open class GuildAutocompleteContext(
    sender: User,
    channelId: Snowflake,
    data: InteractionData,
    arguments: List<CommandArgument<*>>,
    discordInteractionData: DiscordInteraction,
    val guildId: Snowflake,
    val member: Member
) : AutocompleteContext(sender, channelId, data, arguments, discordInteractionData) {
    val appPermissions = discordInteractionData.appPermissions.value ?: error("App Permissions field is null on a Guild Interaction! Bug?")
}