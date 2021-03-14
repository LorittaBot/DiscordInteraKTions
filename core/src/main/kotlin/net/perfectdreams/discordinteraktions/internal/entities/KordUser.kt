package net.perfectdreams.discordinteraktions.internal.entities

import dev.kord.common.entity.DiscordUser
import dev.kord.common.entity.Snowflake
import net.perfectdreams.discordinteraktions.api.entities.User

class KordUser(val handle: DiscordUser) : User {
    override val id: Snowflake
        get() = handle.id
    override val username: String
        get() = handle.username
    override val discriminator: String
        get() = handle.discriminator
    override val avatar: String?
        get() = handle.avatar
    override val bot: Boolean
        get() = handle.bot.discordBoolean
}