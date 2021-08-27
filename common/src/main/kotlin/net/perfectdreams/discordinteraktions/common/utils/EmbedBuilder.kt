package net.perfectdreams.discordinteraktions.common.utils

fun embed(embed: EmbedBuilder.() -> Unit): EmbedBuilder = EmbedBuilder().apply(embed)

@InteraKTionsDslMarker
class EmbedBuilder {
    var title: String? = null
    var description: String? = null
    var url: String? = null
    var author: Author? = null
    var image: Image? = null
    var thumbnail: Thumbnail? = null
    var footer: Footer? = null
    var fields = mutableListOf<Field>()
    var color: Color? = null

    fun author(name: String, url: String? = null, iconUrl: String? = null) {
        author = Author(name, url, iconUrl)
    }

    fun image(url: String) {
        image = Image(url)
    }

    fun thumbnail(url: String) {
        thumbnail = Thumbnail(url)
    }

    fun footer(text: String, iconUrl: String? = null) {
        footer = Footer(text, iconUrl)
    }

    fun field(name: String, value: String, inline: Boolean = false) {
        fields.add(Field(name, value, inline))
    }

    fun inlineField(name: String, value: String) = field(name, value, true)

    fun color(rgb: Int) {
        color = Color(rgb)
    }

    fun color(r: Int, g: Int, b: Int) {
        var rgb: Int = r
        rgb = (rgb shl 8) + g
        rgb = (rgb shl 8) + b
        return color(rgb)
    }

    fun color(color: java.awt.Color) {
        return color(color.rgb)
    }

    data class Author(
        val name: String,
        val url: String?,
        val iconUrl: String?
    )

    data class Image(val url: String)
    data class Thumbnail(val url: String)

    data class Footer(
        val text: String,
        val iconUrl: String?
    )

    data class Field(
        val name: String,
        val value: String,
        val inline: Boolean
    )
}