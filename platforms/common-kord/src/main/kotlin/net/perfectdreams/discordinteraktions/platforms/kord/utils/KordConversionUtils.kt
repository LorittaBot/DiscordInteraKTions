package net.perfectdreams.discordinteraktions.platforms.kord.utils

import dev.kord.rest.builder.message.AllowedMentionsBuilder
import net.perfectdreams.discordinteraktions.api.entities.Snowflake
import net.perfectdreams.discordinteraktions.common.utils.AllowedMentions

/**
 * Converts Discord InteraKTions' Snowflake to Kord's Snowflake
 */
fun Snowflake.toKordSnowflake() = dev.kord.common.entity.Snowflake(this.value)

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