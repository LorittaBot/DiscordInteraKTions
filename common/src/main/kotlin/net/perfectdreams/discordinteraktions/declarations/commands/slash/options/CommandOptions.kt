package net.perfectdreams.discordinteraktions.declarations.commands.slash.options

import net.perfectdreams.discordinteraktions.api.entities.Channel
import net.perfectdreams.discordinteraktions.api.entities.Role
import net.perfectdreams.discordinteraktions.api.entities.User

open class CommandOptions {
    companion object {
        val NO_OPTIONS = object: CommandOptions() {}
    }

    val arguments = mutableListOf<CommandOption<*>>()

    fun string(name: String, description: String) = argument<String>(
        CommandOptionType.String,
        name,
        description
    )

    fun optionalString(name: String, description: String) = argument<String?>(
        CommandOptionType.NullableString,
        name,
        description
    )

    fun integer(name: String, description: String) = argument<Long>(
        CommandOptionType.Integer,
        name,
        description
    )

    fun optionalInteger(name: String, description: String) = argument<Long?>(
        CommandOptionType.NullableInteger,
        name,
        description
    )

    fun number(name: String, description: String) = argument<Double>(
        CommandOptionType.Number,
        name,
        description
    )

    fun optionalNumber(name: String, description: String) = argument<Double?>(
        CommandOptionType.NullableNumber,
        name,
        description
    )

    fun boolean(name: String, description: String) = argument<Boolean>(
        CommandOptionType.Bool,
        name,
        description
    )

    fun optionalBoolean(name: String, description: String) = argument<Boolean?>(
        CommandOptionType.NullableBool,
        name,
        description
    )

    fun user(name: String, description: String) = argument<User>(
        CommandOptionType.User,
        name,
        description
    )

    fun optionalUser(name: String, description: String) = argument<User?>(
        CommandOptionType.NullableUser,
        name,
        description
    )

    fun channel(name: String, description: String) = argument<Channel>(
        CommandOptionType.Channel,
        name,
        description
    )

    fun optionalChannel(name: String, description: String) = argument<Channel?>(
        CommandOptionType.NullableChannel,
        name,
        description
    )

    fun role(name: String, description: String) = argument<Role>(
        CommandOptionType.Role,
        name,
        description
    )

    fun optionalRole(name: String, description: String) = argument<Role?>(
        CommandOptionType.NullableRole,
        name,
        description
    )

    private fun <T> argument(type: CommandOptionType, name: String, description: String) = CommandOptionBuilder<T>(
        type,
        name,
        description,
        mutableListOf()
    )

    fun <T> CommandOptionBuilder<T>.register(): CommandOption<T> {
        if (arguments.any { it.name == this.name })
            throw IllegalArgumentException("Duplicate argument!")

        val option = CommandOption(
            this.type,
            this.name,
            this.description,
            this.choices
        )

        arguments.add(option)
        return option
    }
}