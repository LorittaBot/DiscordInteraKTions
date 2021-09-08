package net.perfectdreams.discordinteraktions.common

import java.io.InputStream

// From Kord
class NamedFile(val name: String, val inputStream: InputStream) {
    val url: String get() = "attachment://$name"

    operator fun component1() = name
    operator fun component2() = inputStream
    operator fun component3() = url
}