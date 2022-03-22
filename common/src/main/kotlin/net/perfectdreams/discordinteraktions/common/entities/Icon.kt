package net.perfectdreams.discordinteraktions.common.entities

import dev.kord.common.entity.Snowflake
import dev.kord.rest.Image
import dev.kord.rest.route.CdnUrl
import dev.kord.rest.route.DiscordCdn
import io.ktor.http.*

// Inspired by Kord's Icon class https://github.com/kordlib/kord/blob/099f0b98c6bad4ee10dd75d482b900133f9c1456/core/src/main/kotlin/entity/Icon.kt
public sealed class Icon(public val animated: Boolean, public val cdnUrl: CdnUrl) {
    public val format: Image.Format
        get() = when {
            animated -> Image.Format.GIF
            else -> Image.Format.WEBP
        }

    override fun toString(): String {
        return "Icon(type=${javaClass.name},animated=$animated,cdnUrl=$cdnUrl)"
    }

    public class EmojiIcon(animated: Boolean, emojiId: Snowflake) :
        Icon(animated, DiscordCdn.emoji(emojiId))

    public class DefaultUserAvatar(discriminator: Int) :
        Icon(false, DiscordCdn.defaultAvatar(discriminator))

    public class UserAvatar(userId: Snowflake, avatarHash: String) :
        Icon(avatarHash.startsWith("a_"), DiscordCdn.userAvatar(userId, avatarHash))

    public class MemberAvatar(guildId: Snowflake, userId: Snowflake, avatarHash: String) :
        Icon(avatarHash.startsWith("a_"), DiscordCdn.memberAvatar(guildId, userId, avatarHash))

    public class RoleIcon(roleId: Snowflake, iconHash: String) :
        Icon(iconHash.startsWith("a_"), DiscordCdn.roleIcon(roleId, iconHash))

}
