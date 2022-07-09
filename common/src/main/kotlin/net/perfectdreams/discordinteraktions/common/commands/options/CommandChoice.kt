package net.perfectdreams.discordinteraktions.common.commands.options

import dev.kord.common.Locale

class CommandChoice<T>(
    val name: String,
    val value: T,
    val nameLocalizations: Map<Locale, String>?
)