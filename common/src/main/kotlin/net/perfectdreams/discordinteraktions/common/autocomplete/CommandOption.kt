package net.perfectdreams.discordinteraktions.common.autocomplete

data class CommandOption<T>(
    val name: String,
    val value: T
)