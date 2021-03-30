package net.perfectdreams.discordinteraktions.declarations.slash

interface CommandChoice<T> {
	val name: String
	val value: T
}

data class DefaultCommandChoice<T>(override val name: String, override val value: T): CommandChoice<T>

data class StringCommandChoice(override val name: String, override val value: String): CommandChoice<String>

data class IntegerCommandChoice(override val name: String, override val value: Int): CommandChoice<Int>
