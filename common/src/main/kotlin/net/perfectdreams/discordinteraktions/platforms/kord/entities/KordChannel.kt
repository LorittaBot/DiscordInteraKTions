package net.perfectdreams.discordinteraktions.platforms.kord.entities

import dev.kord.common.entity.DiscordChannel
import net.perfectdreams.discordinteraktions.api.entities.Channel

class KordChannel(val channel: DiscordChannel) : Channel {
    override val id by channel::id
    override val type by channel::type
}