package net.perfectdreams.discordinteraktions.utils

import dev.kord.common.entity.DiscordEmbed
import dev.kord.common.entity.optional.Optional
import dev.kord.common.entity.optional.OptionalBoolean
import dev.kord.common.entity.optional.OptionalInt
import dev.kord.rest.builder.message.EmbedBuilder
import java.awt.Color
import java.time.Instant
import java.time.format.DateTimeFormatter

class Embed: RestWrapper<DiscordEmbed>, BuilderWrapper<EmbedBuilder> {

    var author: Author? = null
    var body: Body? = null
    var footer: Footer? = null
    var images: Images? = null

    val fields = mutableListOf<Field>()

    /**
     * This is the method we can use
     * for defining the embed's author
     *
     * @param callback Declaration of the author's data
     */
    fun author(callback: Author.() -> Unit) {
        this.author = Author().apply(callback)
    }

    /**
     * This is the method we can use
     * for defining the embed's body data (title, description, color).
     *
     * @param callback Declaration of the body's data
     */
    fun body(callback: Body.() -> Unit) {
        this.body = Body().apply(callback)
    }

    fun field(callback: Field.() -> Unit) {
        fields.add(Field().apply(callback))
    }

    /**
     * This is the method we can use
     * for defining the embed's images (thumbnail, image).
     *
     * @param callback Declaration of the images
     */
    fun images(callback: Author.() -> Unit) {
        this.author = Author().apply(callback)
    }

    /**
     * This is the method we can use
     * for defining the embed's footer (also including the timestamp).
     *
     * @param callback Declaration of the footer's data
     */
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
            it.fields = fields.map { field -> field.intoBuilder() }.toMutableList()
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
            fields = fields.map { it.intoSerial() }.optional(),
            footer = footer?.intoSerial().optional(),
            image = DiscordEmbed.Image(images?.image.optional()).optional(),
            thumbnail = DiscordEmbed.Image(images?.thumbnail.optional()).optional(),
            timestamp = footer?.timestamp?.toTimestamp().optional()
        )
    }
}

class Author: RestWrapper<DiscordEmbed.Author>, BuilderWrapper<EmbedBuilder.Author> {

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
            it.name = name ?: return null
            it.url = url
            it.icon = image
        }
    }
}

/**
 * This is the body class (just something for improving
 * our DSLs and making them prettier!)
 *
 * This includes the embed's:
 *
 * @property title
 * @property description
 * @property color
 *
 * None of them are actually required.
 */
class Body {

    var title: String? = null
    var description: String? = null
    var color: Color? = null

}

class Field: RestWrapper<DiscordEmbed.Field>, BuilderWrapper<EmbedBuilder.Field> {

    // Required
    var name: String = EmbedBuilder.ZERO_WIDTH_SPACE
    // Required
    var value: String = EmbedBuilder.ZERO_WIDTH_SPACE
    // Optional (pre-defined)
    var inline: Boolean = true

    override fun intoSerial(): DiscordEmbed.Field {
        return DiscordEmbed.Field(name, value, OptionalBoolean.Value(inline))
    }

    override fun intoBuilder(): EmbedBuilder.Field {
        return EmbedBuilder.Field().also {
            it.name = name
            it.value = value
            it.inline = inline
        }
    }
}

class Images: BuilderWrapper<EmbedBuilder.Thumbnail> {

    var image: String? = null
    var thumbnail: String? = null

    override fun intoBuilder(): EmbedBuilder.Thumbnail? {
        return EmbedBuilder.Thumbnail().also {
            it.url = thumbnail ?: return null
        }
    }
}

class Footer: RestWrapper<DiscordEmbed.Footer>, BuilderWrapper<EmbedBuilder.Footer> {

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
            it.text = text ?: return null
            it.icon = url
        }
    }
}

/**
 * Just a workaround to convert objects
 * into [Optional] (object from Kord)
 */
private fun <T : Any> Any?.optional(): Optional<T> {
    return if (this == null) {
        Optional()
    } else {
        Optional(this as T)
    }
}

/**
 * Just a workaround to convert integers
 * into [OptionalInt] (object from Kord)
 */
private fun Int?.optionalInt(): OptionalInt {
    return if (this == null) {
        OptionalInt.Missing
    } else {
        OptionalInt.Value(this)
    }
}

fun embed(embed: Embed.() -> Unit): Embed = Embed().apply(embed)

/**
 * Just a way of converting Instant objects into
 * ISO8601 data format (the one which discord accepts)
 */
fun Instant.toTimestamp(): String = DateTimeFormatter.ISO_INSTANT.format(this)

/**
 * This represents an object that can be transformed
 * into a serializable object (wrappers for the Discord API)
 *
 * We are currently using the Kord wrapper objects
 * on the "common" module.
 *
 * @param T The object that it'll transform into
 */
interface RestWrapper<T> {
    fun intoSerial(): T?
}

/**
 * This is just a temporary workaround for objects that can be
 * transformed into Kord's Embed Builder class and inner classes too.
 *
 * (Since they can be transformed to EmbedRequests, so we don't
 * need to create our own wrapper)
 *
 * This could be replaced if we were using Kord's
 * core module, since we can use the EmbedData object.
 *
 * @param T
 */
interface BuilderWrapper<T> {
    fun intoBuilder(): T?
}