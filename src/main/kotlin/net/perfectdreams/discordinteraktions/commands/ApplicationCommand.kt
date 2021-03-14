package net.perfectdreams.discordinteraktions.commands

import kotlinx.serialization.Serializable

@Serializable
class ApplicationCommand(
    val name: String,
    val description: String,
    val options: List<ApplicationCommandOption>
)