package net.perfectdreams.discordinteraktions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class CommandOption {
    abstract val name: String
    abstract val type: Int
}

@Serializable
@SerialName("3")
class StringCommandOption(
    override val name: String,
    override val type: Int,
    val value: String
) : CommandOption()

@Serializable
@SerialName("4")
class IntegerCommandOption(
    override val name: String,
    override val type: Int,
    val value: Int
) : CommandOption()


@Serializable
@SerialName("5")
class BooleanCommandOption(
    override val name: String,
    override val type: Int,
    val value: Boolean
) : CommandOption()

@Serializable
@SerialName("6")
class UserCommandOption(
    override val name: String,
    override val type: Int,
    val value: Long
) : CommandOption()

@Serializable
@SerialName("7")
class ChannelCommandOption(
    override val name: String,
    override val type: Int,
    val value: Long
) : CommandOption()

@Serializable
@SerialName("8")
class RoleCommandOption(
    override val name: String,
    override val type: Int,
    val value: Long
) : CommandOption()
