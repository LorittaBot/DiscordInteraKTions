package net.perfectdreams.discordinteraktions

import net.perfectdreams.discordinteraktions.commands.InteractionCommand

open class CommandArgument<ValueType>(
    val name: String,
    val description: String,
    val type: Int
) {
    var required: Boolean = true
    var choices = mutableListOf<CommandChoice>()

    fun optional() = CommandArgument<ValueType?>(
        name, description, type
    ).also {
        it.required = false
        it.choices = this.choices
    }

    fun choice(value: String, name: String): CommandArgument<ValueType> {
        choices.add(CommandChoice(value, name))
        return this
    }

    fun register(command: InteractionCommand): CommandArgument<ValueType> {
        command.arguments.add(this)
        return this
    }
}

class StringCommandArgument(
    name: String,
    description: String
) : CommandArgument<String>(name, description, 3)

class IntegerCommandArgument(
    name: String,
    description: String
) : CommandArgument<Int>(name, description, 4)

class BooleanCommandArgument(
    name: String,
    description: String
) : CommandArgument<Boolean>(name, description, 5)

class UserCommandArgument(
    name: String,
    description: String
) : CommandArgument<Long>(name, description, 6)