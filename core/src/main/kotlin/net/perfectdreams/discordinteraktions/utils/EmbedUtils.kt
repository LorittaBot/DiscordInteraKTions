package net.perfectdreams.discordinteraktions.utils

import dev.kord.common.entity.DiscordEmbed
import dev.kord.common.entity.Option
import dev.kord.common.entity.optional.Optional
import dev.kord.common.entity.optional.OptionalBoolean
import dev.kord.common.entity.optional.OptionalInt
import dev.kord.rest.builder.message.EmbedBuilder
import java.awt.Color
import java.time.Instant
import java.time.format.DateTimeFormatter

@SuppressWarnings("unused")
class Embed: RestWrapper<DiscordEmbed, EmbedBuilder> {

    var author: Author? = null
    var body: Body? = null
    var fieldSet: FieldSet? = null
    var footer: Footer? = null
    var images: Images? = null

    fun author(callback: Author.() -> Unit) {
        this.author = Author().apply(callback)
    }

    fun body(callback: Body.() -> Unit) {
        this.body = Body().apply(callback)
    }

    fun fieldSet(callback: FieldSet.() -> Unit) {
        this.fieldSet = FieldSet().apply(callback)
    }

    fun images(callback: Author.() -> Unit) {
        this.author = Author().apply(callback)
    }

    fun footer(callback: Footer.() -> Unit) {
        this.footer = Footer().apply(callback)
    }

    override fun intoBuilder(): EmbedBuilder {
        return EmbedBuilder().also {
            it.author = author?.intoBuilder()
            it.color = body?.color?.rgb?.let { it1 -> dev.kord.common.Color(it1) }
            it.description = body?.description
            it.footer = footer?.intoBuilder()
            it.image = images?.image
            it.thumbnail = images?.intoBuilder()
            it.fields = fieldSet?.fields?.map { it.intoBuilder() }.orEmpty().toMutableList()
            it.timestamp = footer?.timestamp
            it.title = body?.title
        }
    }

    override fun intoSerial(): DiscordEmbed {
        return DiscordEmbed(
            title = body?.title.optional(),
            description = body?.description.optional(),
            author = author?.intoSerial().optional(),
            color = body?.color?.rgb.optionalInt(),
            fields = fieldSet?.fields?.mapNotNull { it.intoSerial() }.optional(),
            footer = footer?.intoSerial().optional(),
            image = DiscordEmbed.Image(images?.image.optional()).optional(),
            thumbnail = DiscordEmbed.Image(images?.thumbnail.optional()).optional(),
            timestamp = footer?.timestamp?.toTimestamp().optional()
        )
    }
}

class Author: RestWrapper<DiscordEmbed.Author, EmbedBuilder.Author> {

    // Required
    var name: String? = null
    // Optional
    var url: String? = null
    // Optional
    var image: String? = null

    override fun intoSerial(): DiscordEmbed.Author? {
        return DiscordEmbed.Author(
            (name ?: return null).optional(),
            url.optional(),
            image.optional()
        )
    }

    override fun intoBuilder(): EmbedBuilder.Author? {
        return EmbedBuilder.Author().also {
            it.name = name ?: return@intoBuilder null
            it.url = url
            it.icon = image
        }
    }
}

class Body {

    var title: String? = null
    var description: String? = null
    var color: Color? = null

}

class FieldSet {

    val fields = mutableListOf<Field>()

    fun field(field: Field) {
        fields.add(field)
    }

    fun field(callback: Field.() -> Unit) {
        field(Field().apply(callback))
    }

    fun field(name: String, value: String, inline: Boolean) {
        field {
            this.name = name
            this.value = value
            this.inline = inline
        }
    }
}

class Field: RestWrapper<DiscordEmbed.Field, EmbedBuilder.Field> {

    // Required
    var name: String = EmbedBuilder.ZERO_WIDTH_SPACE
    // Required
    var value: String = EmbedBuilder.ZERO_WIDTH_SPACE
    // Optional
    var inline: Boolean = true

    override fun intoSerial(): DiscordEmbed.Field? {
        return DiscordEmbed.Field(name ?: return null, value ?: return null, OptionalBoolean.Value(inline))
    }

    override fun intoBuilder(): EmbedBuilder.Field {
        return EmbedBuilder.Field().also {
            it.name = name
            it.value = value
            it.inline = inline
        }
    }
}

class Images: RestWrapper<Unit, EmbedBuilder.Thumbnail> {

    var image: String? = null
    var thumbnail: String? = null

    override fun intoSerial(): Unit? {
        TODO("Not yet implemented")
    }

    override fun intoBuilder(): EmbedBuilder.Thumbnail? {
        return EmbedBuilder.Thumbnail().also {
            it.url = thumbnail ?: return@intoBuilder null
        }
    }
}

class Footer: RestWrapper<DiscordEmbed.Footer, EmbedBuilder.Footer> {

    // Required
    var text: String? = null
    // Optional
    var url: String? = null
    // Optional
    var timestamp: Instant? = null

    override fun intoSerial(): DiscordEmbed.Footer? {
        return DiscordEmbed.Footer(text ?: return null, url.optional())
    }

    override fun intoBuilder(): EmbedBuilder.Footer? {
        return EmbedBuilder.Footer().also {
            it.text = text ?: return@intoBuilder null
            it.icon = url
        }
    }
}

private fun <T : Any> Any?.optional(): Optional<T> {
    return if (this == null) {
        Optional()
    } else {
        Optional(this as T)
    }
}

private fun Int?.optionalInt(): OptionalInt {
    return if (this == null) {
        OptionalInt.Missing
    } else {
        OptionalInt.Value(this)
    }
}

fun Instant.toTimestamp(): String = DateTimeFormatter.ISO_INSTANT.format(this)

fun embed(embed: Embed.() -> Unit): Embed = Embed().apply(embed)

interface RestWrapper<T, B> {
    fun intoSerial(): T?
    fun intoBuilder(): B?
}

fun DiscordEmbed.asHybrid(): Embed = embed {
    author {
        name = this@asHybrid.author.value?.name?.value
        url = this@asHybrid.author.value?.url?.value
        image = this@asHybrid.author.value?.iconUrl?.value
    }
    body {

    }
}