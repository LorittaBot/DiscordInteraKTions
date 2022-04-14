package net.perfectdreams.discordinteraktions.common.commands.options

import dev.kord.common.Locale

sealed class CommandChoice<T>(
    val name: String,
    val value: T,
    val nameLocalizations: Map<Locale, String>?
)

class StringCommandChoice(name: String, value: String, nameLocalizations: Map<Locale, String>?) : CommandChoice<String>(name, value, nameLocalizations)
class IntegerCommandChoice(name: String, value: Long, nameLocalizations: Map<Locale, String>?) : CommandChoice<Long>(name, value, nameLocalizations)
class NumberCommandChoice(name: String, value: Double, nameLocalizations: Map<Locale, String>?) : CommandChoice<Double>(name, value, nameLocalizations)