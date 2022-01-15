package net.perfectdreams.discordinteraktions.common.commands.options

import net.perfectdreams.discordinteraktions.api.entities.Channel
import net.perfectdreams.discordinteraktions.api.entities.Role
import net.perfectdreams.discordinteraktions.api.entities.User

open class ApplicationCommandOptions {
    companion object {
        val NO_OPTIONS = object: ApplicationCommandOptions() {}
    }

    val arguments = mutableListOf<CommandOption<*>>()

    fun string(name: String, description: String) = StringCommandOptionBuilder(
        name,
        description
    )

    fun optionalString(name: String, description: String) = NullableStringCommandOptionBuilder(
        name,
        description
    )

    fun integer(name: String, description: String) = IntegerCommandOptionBuilder(
        name,
        description
    )

    fun optionalInteger(name: String, description: String) = NullableIntegerCommandOptionBuilder(
        name,
        description
    )

    fun number(name: String, description: String) = NumberCommandOptionBuilder(
        name,
        description
    )

    fun optionalNumber(name: String, description: String) = NullableNumberCommandOptionBuilder(
        name,
        description
    )

    fun boolean(name: String, description: String) = BooleanCommandOptionBuilder(
        name,
        description
    )

    fun optionalBoolean(name: String, description: String) = NullableBooleanCommandOptionBuilder(
        name,
        description
    )

    fun user(name: String, description: String) = UserCommandOptionBuilder(
        name,
        description
    )

    fun optionalUser(name: String, description: String) = NullableUserCommandOptionBuilder(
        name,
        description
    )

    fun channel(name: String, description: String) = ChannelCommandOptionBuilder(
        name,
        description
    )

    fun optionalChannel(name: String, description: String) = NullableChannelCommandOptionBuilder(
        name,
        description
    )

    fun role(name: String, description: String) = RoleCommandOptionBuilder(
        name,
        description
    )

    fun optionalRole(name: String, description: String) = NullableRoleCommandOptionBuilder(
        name,
        description
    )

    fun <T, ChoiceableType> CommandOptionBuilder<T, ChoiceableType>.register(): CommandOption<T> {
        if (arguments.any { it.name == this.name })
            throw IllegalArgumentException("Duplicate argument \"${this.name}\"!")

        val option = this.build()

        arguments.add(option)
        return option
    }
}