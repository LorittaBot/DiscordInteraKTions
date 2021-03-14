package net.perfectdreams.discordinteraktions.commands

import kotlinx.serialization.Serializable

@Serializable
class ApplicationCommandOption(
    val type: Int,
    val name: String,
    val description: String,
    val required: Boolean? = false,
    val choices: List<ApplicationCommandOptionChoice>? = listOf(),
    val options: List<ApplicationCommandOption>? = listOf()
)