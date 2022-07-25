package net.perfectdreams.discordinteraktions.common.commands.options

import dev.kord.common.Locale
import net.perfectdreams.discordinteraktions.common.stringhandlers.RawStringData
import net.perfectdreams.discordinteraktions.common.stringhandlers.StringData
import net.perfectdreams.discordinteraktions.common.stringhandlers.StringDataHandlers

class CommandChoiceBuilder<T>(
    val name: StringData<*>,
    val value: T
) {
    var nameLocalizations: Map<Locale, StringData<*>>? = null

    fun nameLocalizations(localizationsMap: Map<Locale, String>) =
        nameLocalizations(
            localizationsMap.entries.associate { it.key to RawStringData(it.value) }
        )

    @JvmName("nameLocalizationsFromProvider")
    fun nameLocalizations(localizationsMap: Map<Locale, StringData<*>>) {
        this.nameLocalizations = localizationsMap
    }

    fun build(handlers: StringDataHandlers) = CommandChoice(
        handlers.provide(name),
        value,
        nameLocalizations?.entries?.associate { it.key to handlers.provide(it.value) }
    )
}