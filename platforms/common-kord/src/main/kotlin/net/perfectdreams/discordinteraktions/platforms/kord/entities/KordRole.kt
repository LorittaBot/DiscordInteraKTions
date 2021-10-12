package net.perfectdreams.discordinteraktions.platforms.kord.entities

import dev.kord.common.entity.DiscordRole
import dev.kord.common.entity.DiscordRoleTags
import dev.kord.common.entity.Permissions
import dev.kord.common.entity.Snowflake
import dev.kord.common.entity.optional.Optional
import net.perfectdreams.discordinteraktions.api.entities.Role

class KordRole(val handle: DiscordRole) : Role {
    override val name: String = handle.name
    override val id: Snowflake = handle.id
    override val color: Int = handle.color
    override val hoist: Boolean = handle.hoist
    override val managed: Boolean = handle.managed
    override val mentionable: Boolean = handle.mentionable
    override val permissions: Permissions = handle.permissions
    override val position: Int = handle.position
    override val tags: Optional<DiscordRoleTags> = handle.tags
}