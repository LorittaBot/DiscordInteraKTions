package net.perfectdreams.discordinteraktions.common.commands.options

sealed class CommandChoice<T>(
    val name: String,
    val value: T
)

class StringCommandChoice(name: String, value: String) : CommandChoice<String>(name, value)
class IntegerCommandChoice(name: String, value: Long) : CommandChoice<Long>(name, value)
class NumberCommandChoice(name: String, value: Double) : CommandChoice<Double>(name, value)