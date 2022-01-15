package net.perfectdreams.discordinteraktions.common.context.commands.slash

import net.perfectdreams.discordinteraktions.common.commands.options.CommandOption
import net.perfectdreams.discordinteraktions.common.commands.options.NullableCommandOption

class SlashCommandArguments(val types: Map<CommandOption<*>, Any?>) {
    operator fun <T> get(argument: CommandOption<T>): T {
        if (!types.containsKey(argument) && argument !is NullableCommandOption)
            throw RuntimeException("Missing argument ${argument.name}!")

        return types[argument] as T
    }
}