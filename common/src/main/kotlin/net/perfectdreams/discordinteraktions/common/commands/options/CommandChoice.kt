package net.perfectdreams.discordinteraktions.common.commands.options

class CommandChoice<T>(
	// We need to store the command option type due to type erasure
    val type: CommandOptionType,
    val name: String,
    val value: T
)