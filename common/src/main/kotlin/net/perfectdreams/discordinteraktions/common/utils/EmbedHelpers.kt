package net.perfectdreams.discordinteraktions.common.utils

// Utilities extension methods for Kord Embeds, mostly providing functions to replace Kord's embed callbacks
fun dev.kord.rest.builder.message.EmbedBuilder.author(name: String, url: String? = null, iconUrl: String? = null) {
    author {
        this.name = name
        this.url = url
        this.icon = iconUrl
    }
}

var dev.kord.rest.builder.message.EmbedBuilder.thumbnailUrl: String?
    get() = this.thumbnail?.url
    set(value) {
        if (value == null) {
            this.thumbnail = null
        } else {
            thumbnail {
                this.url = value
            }
        }
    }

fun dev.kord.rest.builder.message.EmbedBuilder.footer(text: String, iconUrl: String? = null) {
    footer {
        this.text = text
        this.icon = iconUrl
    }
}

fun dev.kord.rest.builder.message.EmbedBuilder.field(name: String, value: String, inline: Boolean = false) = field(name, inline) { value }

fun dev.kord.rest.builder.message.EmbedBuilder.inlineField(name: String, value: String) = field(name, true) { value }