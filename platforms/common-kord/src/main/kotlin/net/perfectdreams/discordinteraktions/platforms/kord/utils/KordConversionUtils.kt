package net.perfectdreams.discordinteraktions.platforms.kord.utils

import dev.kord.common.entity.ResolvedObjects
import dev.kord.common.entity.optional.Optional
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordMember
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordUser
import net.perfectdreams.discordinteraktions.platforms.kord.entities.messages.KordMessage
import net.perfectdreams.discordinteraktions.platforms.kord.entities.messages.KordPublicMessage

/**
 * Converts Kord's Resolved Objects to Discord InteraKTions's Resolved Objects
 */
fun ResolvedObjects.toDiscordInteraKTionsResolvedObjects(): net.perfectdreams.discordinteraktions.common.interactions.ResolvedObjects {
    val users = this.users.value?.map {
        it.key to KordUser(it.value)
    }?.toMap()

    val members = this.members.value?.map {
        // In this case, the user map contains the user object, so we need to get it from there
        it.key to KordMember(
            it.value,
            users?.get(it.key)!! // Should NEVER be null!
        )
    }?.toMap()

    val messages = this.messages.value?.map {
        it.key to KordMessage(
            it.value
        )
    }?.toMap()

    return net.perfectdreams.discordinteraktions.common.interactions.ResolvedObjects(
        users,
        members,
        messages
    )
}

fun <T> runIfNotMissing(optional: Optional<T>, callback: (T?) -> (Unit)) {
    if (optional !is Optional.Missing)
        callback.invoke(optional.value)
}