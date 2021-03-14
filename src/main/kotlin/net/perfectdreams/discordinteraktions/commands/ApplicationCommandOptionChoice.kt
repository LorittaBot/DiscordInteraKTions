package net.perfectdreams.discordinteraktions.commands

import kotlinx.serialization.Serializable

@Serializable
class ApplicationCommandOptionChoice(
    val name: String,
    val value: String
)