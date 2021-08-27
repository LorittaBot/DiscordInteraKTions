package net.perfectdreams.discordinteraktions.declarations.commands.slash.options

class CommandOption<T>(
    // We need to store the command option type due to type erasure
    val type: CommandOptionType,
    val name: String,
    val description: String,
    val choices: List<CommandChoice<T>>
)