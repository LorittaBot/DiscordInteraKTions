package net.perfectdreams.discordinteraktions.internal.entities

import dev.kord.common.entity.DiscordChannel
import net.perfectdreams.discordinteraktions.api.entities.GuildChannel

class KordGuildChannel(handle: DiscordChannel) : GuildChannel, KordChannel(handle) {
    override val name: String
        get() = handle.name.value!!
}