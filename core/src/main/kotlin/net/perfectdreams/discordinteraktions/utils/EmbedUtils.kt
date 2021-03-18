package net.perfectdreams.discordinteraktions.utils

import dev.kord.common.entity.DiscordEmbed
import dev.kord.common.entity.optional.*
import dev.kord.rest.builder.message.EmbedBuilder
import io.ktor.util.*
import java.awt.Color
import java.time.Instant
import java.time.format.DateTimeFormatter
import kotlin.reflect.KProperty

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
            color = body?.color?.rgb?.let { OptionalInt.Value(it) } ?: OptionalInt.Missing,
            fields = fields.map { it.intoSerial() }.optional(),
            footer = footer?.intoSerial().optional(),
            image = images?.image?.let { DiscordEmbed.Image(it.optional()) }.optional(),
            thumbnail = images?.thumbnail?.let { DiscordEmbed.Thumbnail(it.optional()) }.optional(),
            timestamp = footer?.timestamp?.toTimestamp().optional()
        )
    }
}

class Author: RestWrapper<DiscordEmbed.Author>, BuilderWrapper<EmbedBuilder.Author> {

    var name: String? by Component(false)
    var url: String? by Component(false)
    var icon: String? by Component(false)

    override fun intoSerial(): DiscordEmbed.Author {
        return DiscordEmbed.Author(
            name.optional(),
            url.optional(),
            icon.optional()
        )
    }

    override fun intoBuilder(): EmbedBuilder.Author {
        return EmbedBuilder.Author().also {
            it.name = name
            it.url = url
            it.icon = icon
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

    var title: String? by Component(false)
    var description: String? by Component(false)
    var color: Color? by Component(false)

}

class Field: RestWrapper<DiscordEmbed.Field>, BuilderWrapper<EmbedBuilder.Field> {

    var name: String? by Component(true)
    var value: String? by Component(true)
    var inline: Boolean? by Component(false)

    override fun intoSerial(): DiscordEmbed.Field {
        return DiscordEmbed.Field(
            name!!,
            value!!,
            OptionalBoolean.Value(inline ?: false)
        )
    }

    override fun intoBuilder(): EmbedBuilder.Field {
        return EmbedBuilder.Field().also {
            it.name = name!!
            it.value = value!!
            it.inline = inline
        }
    }
}

class Images: BuilderWrapper<EmbedBuilder.Thumbnail> {

    var image: String? by Component(false)
    var thumbnail: String? by Component(false)

    override fun intoBuilder(): EmbedBuilder.Thumbnail? {
        return EmbedBuilder.Thumbnail().also {
            it.url = thumbnail ?: return null
        }
    }
}

class Footer: RestWrapper<DiscordEmbed.Footer>, BuilderWrapper<EmbedBuilder.Footer> {

    var text: String? by Component(true)
    var icon: String? by Component(false)
    var timestamp: Instant? by Component(false)

    override fun intoSerial(): DiscordEmbed.Footer {
        return DiscordEmbed.Footer(
            text!!,
            icon.optional()
        )
    }

    override fun intoBuilder(): EmbedBuilder.Footer {
        return EmbedBuilder.Footer().also {
            it.text = text!!
            it.icon = icon
        }
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
    fun intoSerial(): T
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

class Component<V>(val required: Boolean) {
    private var value: V? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>): V? {
        if (required && value == null)
            error("Value '${property.name}' is required.")
        else return value
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: V) {
        this.value = value
    }
}

private fun <T> T?.optional(): Optional<T> =
    if (this != null) Optional.Value(this) else Optional.Missing()