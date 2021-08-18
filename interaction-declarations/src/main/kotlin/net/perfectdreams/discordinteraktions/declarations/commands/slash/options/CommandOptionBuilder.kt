package net.perfectdreams.discordinteraktions.declarations.commands.options

import net.perfectdreams.discordinteraktions.declarations.commands.CommandChoice

open class CommandOptionBuilder<T>(
    // We need to store the command option type due to type erasure
    val type: CommandOptionType,
    val name: String,
    val description: String,
    val choices: MutableList<CommandChoice<T>>
) {
    fun choice(value: T, name: String): CommandOptionBuilder<T> {
        choices.add(
            CommandChoice(
                type,
                name,
                value
            )
        )
        return this
    }
}