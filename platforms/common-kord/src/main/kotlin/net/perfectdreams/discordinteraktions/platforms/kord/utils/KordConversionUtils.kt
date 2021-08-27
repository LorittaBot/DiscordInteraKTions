package net.perfectdreams.discordinteraktions.platforms.kord.utils

import dev.kord.common.Color
import dev.kord.common.entity.ButtonStyle
import dev.kord.common.entity.ResolvedObjects
import dev.kord.rest.builder.component.ActionRowBuilder
import dev.kord.rest.builder.message.AllowedMentionsBuilder
import net.perfectdreams.discordinteraktions.api.entities.Snowflake
import net.perfectdreams.discordinteraktions.common.utils.ActionRowComponent
import net.perfectdreams.discordinteraktions.common.utils.AllowedMentions
import net.perfectdreams.discordinteraktions.common.utils.ButtonComponent
import net.perfectdreams.discordinteraktions.common.utils.EmbedBuilder
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordMember
import net.perfectdreams.discordinteraktions.platforms.kord.entities.messages.KordPublicMessage
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
    val users = this.users.value?.map {
        it.key.toDiscordInteraKTionsSnowflake() to KordUser(it.value)
    }?.toMap()

    val members = this.members.value?.map {
        // In this case, the user map contains the user object, so we need to get it from there
        it.key.toDiscordInteraKTionsSnowflake() to KordMember(
            it.value,
            users?.get(it.key.toDiscordInteraKTionsSnowflake())!! // Should NEVER be null!
        )
    }?.toMap()

    val messages = this.messages.value?.map {
        it.key.toDiscordInteraKTionsSnowflake() to KordPublicMessage(
            it.value
        )
    }?.toMap()

    return net.perfectdreams.discordinteraktions.common.interactions.ResolvedObjects(
        users,
        members,
        messages
    )
}

/**
 * Converts Discord InteraKTions's Action Row to Kord's Action Row Builder
 */
fun ActionRowComponent.toKordActionRowBuilder(): ActionRowBuilder {
    val builder = ActionRowBuilder()

    this.components.map {
        when (it) {
            is ActionRowComponent -> TODO()
            is ButtonComponent -> {
                when (it.style) {
                    net.perfectdreams.discordinteraktions.common.components.buttons.ButtonStyle.Link -> {
                        val url = it.url
                        require(url != null) { "Button URL must not be null!" }

                        builder.linkButton(url) {
                            this.label = it.label
                            this.disabled = it.disabled
                        }
                    }
                    else -> {
                        val customId = it.customId
                        require(customId != null) { "Button Custom ID must not be null!" }

                        builder.interactionButton(
                            when (it.style) {
                                // This should never happen because the style is already being processed above
                                net.perfectdreams.discordinteraktions.common.components.buttons.ButtonStyle.Link -> error("This should never happen!")
                                net.perfectdreams.discordinteraktions.common.components.buttons.ButtonStyle.Danger -> ButtonStyle.Danger
                                net.perfectdreams.discordinteraktions.common.components.buttons.ButtonStyle.Primary -> ButtonStyle.Primary
                                net.perfectdreams.discordinteraktions.common.components.buttons.ButtonStyle.Secondary -> ButtonStyle.Secondary
                                net.perfectdreams.discordinteraktions.common.components.buttons.ButtonStyle.Success -> ButtonStyle.Success
                                is net.perfectdreams.discordinteraktions.common.components.buttons.ButtonStyle.Unknown -> ButtonStyle.Unknown(it.type)
                            },
                            customId
                        ) {
                            this.label = it.label
                            this.disabled = it.disabled
                        }
                    }
                }
            }
        }
    }

    return builder
}

/**
 * Converts Discord InteraKTions' Embed to Kord's Embed
 */
fun EmbedBuilder.toKordEmbedBuilder(): dev.kord.rest.builder.message.EmbedBuilder {
    return dev.kord.rest.builder.message.EmbedBuilder().also { kordBuilder ->
        kordBuilder.title = this.title
        kordBuilder.description = this.description
        kordBuilder.url = this.url

        val color = this.color
        val author = this.author
        val image = this.image
        val thumbnail = this.thumbnail
        val footer = this.footer
        val fields = this.fields

        if (color != null)
            kordBuilder.color = Color(color.rgb)

        if (author != null)
            kordBuilder.author {
                this.name = author.name
                this.url = author.url
                this.icon = author.iconUrl
            }

        if (image != null)
            kordBuilder.image = image.url

        if (thumbnail != null)
            kordBuilder.thumbnail { this.url = thumbnail.url }

        if (footer != null)
            kordBuilder.footer {
                this.text = footer.text
                this.icon = footer.iconUrl
            }

        fields.forEach {
            kordBuilder.field {
                this.name = it.name
                this.value = it.value
                this.inline = it.inline
            }
        }
    }
}