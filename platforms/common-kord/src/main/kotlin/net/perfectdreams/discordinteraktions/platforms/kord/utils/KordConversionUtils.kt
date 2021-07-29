package net.perfectdreams.discordinteraktions.platforms.kord.utils

import dev.kord.common.entity.ResolvedObjects
import dev.kord.rest.builder.message.AllowedMentionsBuilder
import net.perfectdreams.discordinteraktions.api.entities.Snowflake
import net.perfectdreams.discordinteraktions.common.utils.AllowedMentions
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordUser

/**
 * Converts Discord InteraKTions' Snowflake to Kord's Snowflake
 */
fun Snowflake.toKordSnowflake() = dev.kord.common.entity.Snowflake(this.value)

/**
 * Converts Kord's Snowflake to Discord InteraKTions's Snowflake
 */
fun dev.kord.common.entity.Snowflake.toDiscordInteraKTionsSnowflake() = Snowflake(this.value)

/**
 * Converts Discord InteraKTions' Allowed Mentions to Kord's Allowed Mentions Builder
 */
fun AllowedMentions.toKordAllowedMentions(): AllowedMentionsBuilder {
    return AllowedMentionsBuilder().apply {
        this.repliedUser = this@toKordAllowedMentions.repliedUser

        for (roleId in this@toKordAllowedMentions.roles) {
            this.roles.add(roleId.toKordSnowflake())
        }

        for (userId in this@toKordAllowedMentions.users) {
            this.users.add(userId.toKordSnowflake())
        }
    }
}

/**
 * Converts Kord's Resolved Objects to Discord InteraKTions's Resolved Objects
 */
fun ResolvedObjects.toDiscordInteraKTionsResolvedObjects(): net.perfectdreams.discordinteraktions.common.interactions.ResolvedObjects {
    return net.perfectdreams.discordinteraktions.common.interactions.ResolvedObjects(
        this.users.value?.map {
            it.key.toDiscordInteraKTionsSnowflake() to KordUser(it.value)
        }?.toMap()
    )
}