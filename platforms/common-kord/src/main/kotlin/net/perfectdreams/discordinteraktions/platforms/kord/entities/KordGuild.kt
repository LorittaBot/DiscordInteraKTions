package net.perfectdreams.discordinteraktions.platforms.kord.entities

import dev.kord.common.entity.DiscordGuild
import net.perfectdreams.discordinteraktions.api.entities.Guild
import net.perfectdreams.discordinteraktions.api.entities.Snowflake

class KordGuild(private val guild: DiscordGuild) : Guild {
    override val id = Snowflake(guild.id.value)
}