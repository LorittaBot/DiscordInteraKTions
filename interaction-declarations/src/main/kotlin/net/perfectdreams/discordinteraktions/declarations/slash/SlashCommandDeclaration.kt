package net.perfectdreams.discordinteraktions.declarations.slash

import net.perfectdreams.discordinteraktions.api.entities.Channel
import net.perfectdreams.discordinteraktions.api.entities.Role
import net.perfectdreams.discordinteraktions.api.entities.User

abstract class SlashCommandDeclaration {
    abstract val name: String
    abstract val description: String
    open val options: Options = Options.NO_OPTIONS

    abstract class Options {
        companion object {
            val NO_OPTIONS = object: Options() {}
        }
        
        val arguments = mutableListOf<CommandOption<*>>()

        fun string(name: String, description: String) = CommandOption<String?>(
            3,
            name,
            description,
            false,
            listOf()
        )

        fun integer(name: String, description: String) = CommandOption<Int?>(
            4,
            name,
            description,
            false,
            listOf()
        )

        fun boolean(name: String, description: String) = CommandOption<Boolean?>(
            5,
            name,
            description,
            false,
            listOf()
        )

        fun user(name: String, description: String) = CommandOption<User?>(
            6,
            name,
            description,
            false,
            listOf()
        )

        fun channel(name: String, description: String) = CommandOption<Channel?>(
            7,
            name,
            description,
            false,
            listOf()
        )

        fun role(name: String, description: String) = CommandOption<Role?>(
            8,
            name,
            description,
            false,
            listOf()
        )

        fun <T : CommandOption<*>> T.register(): T {
            val duplicate = arguments.any { it.name == this.name }
            if (duplicate)
                throw IllegalArgumentException("Duplicate argument!")

            arguments.add(this)
            return this
        }
    }
}