package net.perfectdreams.discordinteraktions.entities.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.perfectdreams.discordinteraktions.CommandOption
import net.perfectdreams.discordinteraktions.entities.Member
import net.perfectdreams.discordinteraktions.entities.User

@Serializable
sealed class InteractionRequest {
    abstract val type: Int
}

@Serializable
@SerialName("1")
class PingRequest(
    override val type: Int
) : InteractionRequest()

@Serializable
@SerialName("2")
data class ApplicationCommandRequest(
    override val type: Int,
    @SerialName("channel_id")
    val channelId: Long,
    val data: CommandInfo,
    @SerialName("guild_id")
    val guildId: Long? = null, // Can be null if used in a DM
    val member: Member? = null, // Also can be null if used in a DM
    val user: User? = null, // Can be null if used in not used in a DM
    val id: Long,
    val token: String
) : InteractionRequest() {
    @Serializable
    data class CommandInfo(
        val id: Long,
        val name: String,
        val options: List<CommandOption> = listOf()
    )
}