package net.perfectdreams.discordinteraktions.entities

import dev.kord.common.entity.DiscordApplicationCommandInteractionData
import dev.kord.common.entity.DiscordInteractionGuildMember
import dev.kord.common.entity.DiscordUser
import dev.kord.common.entity.InteractionType
import dev.kord.common.entity.Snowflake
import dev.kord.common.entity.optional.Optional
import dev.kord.common.entity.optional.OptionalSnowflake
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Classes from Kord but changed to match what we need for DiscordInteraKTions
//
// Because some stuff (like ping) are not available yet in Kord!
@Serializable
sealed class Interaction {
    abstract val id: Snowflake
    abstract val token: String
    abstract val type: InteractionType
    abstract val version: Int
}

@Serializable
@SerialName("1")
data class PingInteraction(
    override val id: Snowflake,
    override val token: String,
    override val type: InteractionType,
    override val version: Int,
    val user: DiscordUser
) : Interaction()

@Serializable
@SerialName("2")
data class CommandInteraction(
    override val id: Snowflake,
    override val type: InteractionType,
    val data: DiscordApplicationCommandInteractionData,
    @SerialName("guild_id")
    val guildId: OptionalSnowflake = OptionalSnowflake.Missing,
    @SerialName("channel_id")
    val channelId: Snowflake,
    val member: Optional<DiscordInteractionGuildMember> = Optional.Missing(),
    val user: Optional<DiscordUser> = Optional.Missing(),
    override val token: String,
    override val version: Int,
) : Interaction()