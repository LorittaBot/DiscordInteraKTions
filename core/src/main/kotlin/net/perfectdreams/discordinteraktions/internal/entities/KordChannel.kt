package net.perfectdreams.discordinteraktions.internal.entities

import dev.kord.common.entity.ChannelType
import dev.kord.common.entity.DiscordChannel
import dev.kord.common.entity.Snowflake
import net.perfectdreams.discordinteraktions.api.entities.Channel

open class KordChannel(val handle: DiscordChannel) : Channel {
    companion object {
        fun from(channel: DiscordChannel): KordChannel {
            return when (channel.type) {
                ChannelType.GuildText -> KordGuildChannel(channel)
                else -> KordChannel(channel)
            }
        }
    }
    override val id by handle::id
}