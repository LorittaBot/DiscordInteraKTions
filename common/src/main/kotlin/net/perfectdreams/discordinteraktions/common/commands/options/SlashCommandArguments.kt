package net.perfectdreams.discordinteraktions.common.commands.options

class SlashCommandArguments(val types: Map<OptionReference<*>, Any?>) {
    operator fun <T> get(argument: OptionReference<T>): T {
        if (!types.containsKey(argument) && argument.required)
            throw RuntimeException("Missing argument ${argument.name}!")

        return types[argument] as T
    }
}