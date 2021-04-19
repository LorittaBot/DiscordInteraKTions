package net.perfectdreams.discordinteraktions.internal.entities

import dev.kord.common.entity.DiscordRole
import dev.kord.common.entity.DiscordUser
import dev.kord.common.entity.Snowflake
import net.perfectdreams.discordinteraktions.api.entities.Role
import net.perfectdreams.discordinteraktions.api.entities.User

class KordRole(val handle: DiscordRole) : Role {
    override val id by handle::id
    override val name by handle::name
    override val color by handle::color
    override val hoist by handle::hoist
    override val position by handle::position
    override val permissions: Long
        get() = 0L // TODO: Fix handle.permissions.code.value
    override val managed by handle::managed
    override val mentionable by handle::mentionable
    override val tags: Any
        get() = handle.tags
}