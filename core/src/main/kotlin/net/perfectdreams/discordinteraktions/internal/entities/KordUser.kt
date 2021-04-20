package net.perfectdreams.discordinteraktions.internal.entities

import dev.kord.common.entity.DiscordUser
import dev.kord.common.entity.Snowflake
import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.api.entities.UserAvatar

class KordUser(val handle: DiscordUser) : User {
    override val id by handle::id
    override val user by handle::username
    override val discriminator by handle::discriminator
    override val avatar = UserAvatar(
        id.value,
        discriminator.toInt(),
        handle.avatar
    )
    override val bot: Boolean
        get() = handle.bot.discordBoolean
}