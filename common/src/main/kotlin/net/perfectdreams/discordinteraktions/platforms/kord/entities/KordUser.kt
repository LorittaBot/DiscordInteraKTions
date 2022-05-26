package net.perfectdreams.discordinteraktions.platforms.kord.entities

import dev.kord.common.entity.DiscordUser
import dev.kord.common.entity.UserFlag
import dev.kord.common.entity.UserFlags
import net.perfectdreams.discordinteraktions.common.entities.Icon
import net.perfectdreams.discordinteraktions.common.entities.User

class KordUser(val handle: DiscordUser) : User {
    override val id = handle.id
    override val name by handle::username
    override val discriminator by handle::discriminator
    override val avatarHash = handle.avatar
    override val avatar = avatarHash?.let {
        Icon.UserAvatar(id, it)
    } ?: Icon.DefaultUserAvatar(discriminator.toInt())
    override val bot: Boolean
        get() = handle.bot.discordBoolean
    override val publicFlags: List<UserFlag>
            get() = handle.publicFlags.value?.flags ?: emptyList()
}