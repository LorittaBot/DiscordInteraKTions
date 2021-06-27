package net.perfectdreams.discordinteraktions.platform.jda.entities

import net.perfectdreams.discordinteraktions.api.entities.Guild
import net.perfectdreams.discordinteraktions.api.entities.Snowflake

class JDAGuild(private val guild: net.dv8tion.jda.api.entities.Guild) : Guild {
    override val id = Snowflake(guild.idLong)
}