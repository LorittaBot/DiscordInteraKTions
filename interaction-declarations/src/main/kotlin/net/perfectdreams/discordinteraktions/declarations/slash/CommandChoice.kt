package net.perfectdreams.discordinteraktions.declarations.slash

import net.perfectdreams.discordinteraktions.declarations.slash.options.CommandOptionType

class CommandChoice<T>(
	// We need to store the command option type due to type erasure
	val type: CommandOptionType,
	val name: String,
	val value: T
)