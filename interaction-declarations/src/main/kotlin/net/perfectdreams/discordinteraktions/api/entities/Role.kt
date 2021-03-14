package net.perfectdreams.discordinteraktions.api.entities

import dev.kord.common.entity.Snowflake

interface Role {
	// id=Snowflake(value=297051132793061378), name=new role, color=15277667, hoist=false, position=60, permissions=Permissions(values=DiscordBitSet(100100100111101111101100011000011)), managed=false, mentionable=true, tags=Optional.Missing)}
	val id: Snowflake
	val name: String
	val color: Int
	val hoist: Boolean
	val position: Int
	val permissions: Long
	val managed: Boolean
	val mentionable: Boolean
	val tags: Any
}

inline val Role.idLong get() = id.value
