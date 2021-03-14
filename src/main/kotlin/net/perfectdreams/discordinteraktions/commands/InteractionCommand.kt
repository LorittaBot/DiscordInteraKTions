package net.perfectdreams.discordinteraktions.commands

import net.perfectdreams.discordinteraktions.BooleanCommandOption
import net.perfectdreams.discordinteraktions.ChannelCommandOption
import net.perfectdreams.discordinteraktions.CommandArgument
import net.perfectdreams.discordinteraktions.IntegerCommandOption
import net.perfectdreams.discordinteraktions.RoleCommandOption
import net.perfectdreams.discordinteraktions.StringCommandArgument
import net.perfectdreams.discordinteraktions.StringCommandOption
import net.perfectdreams.discordinteraktions.UserCommandOption
import net.perfectdreams.discordinteraktions.context.InteractionCommandContext
import java.lang.RuntimeException

abstract class InteractionCommand(val label: String, val description: String) {
	val arguments = mutableListOf<CommandArgument<*>>()

	fun string(name: String, description: String) = StringCommandArgument(name, description).also {
		arguments.add(it)
	}

	abstract suspend fun executes(context: InteractionCommandContext)

	fun <T> optionValue(context: InteractionCommandContext, argument: CommandArgument<T>): T {
		val result = context.request.data.options.firstOrNull { it.name == argument.name && it.type == argument.type }

		if (result != null) {
			return when (argument.type) {
				3 -> (result as StringCommandOption).value as T
				4 -> (result as IntegerCommandOption).value as T
				5 -> (result as BooleanCommandOption).value as T
				6 -> (result as UserCommandOption).value as T
				7 -> (result as ChannelCommandOption).value as T
				8 -> (result as RoleCommandOption).value as T
				else -> throw RuntimeException("Unsupported option type ${argument.type} for ${argument.name}")
			}
		} else if (argument.required)
			throw RuntimeException("Missing value for ${argument.name} but it is required!")

		return null as T
	}
}