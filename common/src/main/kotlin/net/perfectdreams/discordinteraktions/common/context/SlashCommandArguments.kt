package net.perfectdreams.discordinteraktions.common.context

import net.perfectdreams.discordinteraktions.declarations.slash.options.CommandOption
import net.perfectdreams.discordinteraktions.declarations.slash.options.CommandOptionType

class SlashCommandArguments(val types: Map<CommandOption<*>, Any?>) {
    operator fun <T> get(argument: CommandOption<T>): T {
        if (!types.containsKey(argument) && argument.type is CommandOptionType.ToNullable)
            throw RuntimeException("Missing argument ${argument.name}!")

        return types[argument] as T
    }
}