package net.perfectdreams.discordinteraktions.internal.entities

import dev.kord.common.entity.DiscordUser
import dev.kord.common.entity.Snowflake
import net.perfectdreams.discordinteraktions.api.entities.User

class KordUser(val handle: DiscordUser) : User {
    override val id by handle::id
    override val username by handle::username
    override val discriminator by handle::discriminator
    override val avatar by handle::avatar
    override val bot: Boolean
        get() = handle.bot.discordBoolean
}