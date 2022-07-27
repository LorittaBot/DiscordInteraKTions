package net.perfectdreams.discordinteraktions.common.commands.options

import dev.kord.common.Locale

class CommandChoiceBuilder<T>(
    val name: String,
    val value: T
) {
    var nameLocalizations: Map<Locale, String>? = null

    fun build() = CommandChoice(
        name,
        value,
        nameLocalizations
    )
}