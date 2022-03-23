package net.perfectdreams.discordinteraktions.common.autocomplete

import dev.kord.common.entity.CommandArgument
import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.Snowflake
import net.perfectdreams.discordinteraktions.common.entities.User
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData

// This doesn't inherit from InteractionContext because we can't send messages on a autocomplete request
open class AutocompleteContext(
    val sender: User,
    val channelId: Snowflake,
    val data: InteractionData,
    val arguments: List<CommandArgument<*>>,

    /**
     * The interaction data object from Discord, useful if you need to use data that is not exposed directly via Discord InteraKTions
     */
    val discordInteraction: DiscordInteraction
)