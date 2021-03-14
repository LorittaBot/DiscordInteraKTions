package net.perfectdreams.discordinteraktions.declarations.slash

open class CommandChoice(
    val name: String
)

class StringCommandChoice(
    name: String,
    val value: String
) : CommandChoice(name)

class IntegerCommandChoice(
    name: String,
    val value: Int
) : CommandChoice(name)