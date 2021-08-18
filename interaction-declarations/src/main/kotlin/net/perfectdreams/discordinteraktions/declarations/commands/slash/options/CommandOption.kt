package net.perfectdreams.discordinteraktions.declarations.commands.options

import net.perfectdreams.discordinteraktions.declarations.commands.CommandChoice

class CommandOption<T>(
    // We need to store the command option type due to type erasure
    val type: CommandOptionType,
    val name: String,
    val description: String,
    val choices: List<CommandChoice<T>>
)