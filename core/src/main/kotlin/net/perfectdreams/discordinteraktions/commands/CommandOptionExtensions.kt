package net.perfectdreams.discordinteraktions.commands

import dev.kord.common.entity.CommandArgument
import dev.kord.common.entity.Snowflake
import net.perfectdreams.discordinteraktions.api.entities.Channel
import net.perfectdreams.discordinteraktions.api.entities.Role
import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.context.SlashCommandContext
import net.perfectdreams.discordinteraktions.declarations.slash.CommandOption
import net.perfectdreams.discordinteraktions.internal.entities.KordChannel
import net.perfectdreams.discordinteraktions.internal.entities.KordRole
import net.perfectdreams.discordinteraktions.internal.entities.KordUser

fun <T> CommandOption<T>.get(context: SlashCommandContext) = (context.request.data.options.value?.first { it.name == this.name } as CommandArgument)
    .value.value as T

@JvmName("getUser")
fun CommandOption<User>.get(context: SlashCommandContext): User {
    val option = this as CommandOption<User?>
    return option.get(context) ?: throw IllegalArgumentException("Not resolved")
}

@JvmName("getUser?")
fun CommandOption<User?>.get(context: SlashCommandContext): User? {
    val userId = getRawValueOrNull<String>(context)?.toLong() ?: return null
    val resolved = context.request.data.resolved.value ?: return null
    val resolvedMap = resolved.users.value ?: return null
    val kordInstance = resolvedMap[Snowflake(userId)] ?: return null

    // Now we need to wrap the kord user in our own implementation!
    return KordUser(kordInstance)
}


@JvmName("getChannel")
fun CommandOption<Channel>.get(context: SlashCommandContext): Channel {
    val option = this as CommandOption<Channel?>
    return option.get(context) ?: throw IllegalArgumentException("Not resolved")
}

@JvmName("getChannel?")
fun CommandOption<Channel?>.get(context: SlashCommandContext): Channel? {
    val userId = getRawValueOrNull<String>(context)?.toLong() ?: return null
    val resolved = context.request.data.resolved.value ?: return null
    val resolvedMap = resolved.channels.value ?: return null
    val kordInstance = resolvedMap[Snowflake(userId)] ?: return null

    // Now we need to wrap the kord user in our own implementation!
    return KordChannel.from(kordInstance)
}

@JvmName("getRole")
fun CommandOption<Role>.get(context: SlashCommandContext): Role {
    val option = this as CommandOption<Role?>
    return option.get(context) ?: throw IllegalArgumentException("Not resolved")
}

@JvmName("getRole?")
fun CommandOption<Role?>.get(context: SlashCommandContext): Role? {
    val userId = getRawValueOrNull<String>(context)?.toLong() ?: return null
    val resolved = context.request.data.resolved.value ?: return null
    val resolvedMap = resolved.roles.value ?: return null
    val kordInstance = resolvedMap[Snowflake(userId)] ?: return null

    // Now we need to wrap the kord user in our own implementation!
    return KordRole(kordInstance)
}

fun <T> CommandOption<*>.getRawValue(context: SlashCommandContext) = getRawValueOrNull<T>(context) ?: throw IllegalArgumentException("Option \"$name\" was not found in the interaction response!")

fun <T> CommandOption<*>.getRawValueOrNull(context: SlashCommandContext): T? {
    val element = context.request.data.options.value?.firstOrNull { it.name == this.name } ?: return null
    val commandArgument = element as CommandArgument

    return commandArgument.value.value as T
}