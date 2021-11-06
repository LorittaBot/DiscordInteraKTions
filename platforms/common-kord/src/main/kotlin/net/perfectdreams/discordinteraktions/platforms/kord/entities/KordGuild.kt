package net.perfectdreams.discordinteraktions.platforms.kord.entities

import dev.kord.common.entity.DiscordGuild
import dev.kord.common.entity.Snowflake
import net.perfectdreams.discordinteraktions.api.entities.Guild

class KordGuild(val guild: DiscordGuild) : Guild {
    override val id = guild.id
}