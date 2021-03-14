package net.perfectdreams.discordinteraktions.internal.entities

import dev.kord.common.entity.DiscordRole
import dev.kord.common.entity.DiscordUser
import dev.kord.common.entity.Snowflake
import net.perfectdreams.discordinteraktions.api.entities.Role
import net.perfectdreams.discordinteraktions.api.entities.User

class KordRole(val handle: DiscordRole) : Role {
    override val id: Snowflake
        get() = handle.id
    override val name: String
        get() = handle.name
    override val color: Int
        get() = handle.color
    override val hoist: Boolean
        get() = handle.hoist
    override val position: Int
        get() = handle.position
    override val permissions: Long
        get() = 0L // TODO: Fix handle.permissions.code.value
    override val managed: Boolean
        get() = handle.managed
    override val mentionable: Boolean
        get() = handle.mentionable
    override val tags: Any
        get() = handle.tags
}