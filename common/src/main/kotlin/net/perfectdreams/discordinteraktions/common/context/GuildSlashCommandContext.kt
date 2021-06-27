package net.perfectdreams.discordinteraktions.common.context

import net.perfectdreams.discordinteraktions.api.entities.Guild
import net.perfectdreams.discordinteraktions.api.entities.User

open class GuildSlashCommandContext(
    // TODO:
    // val request: CommandInteraction,
    // val relativeOptions: List<Option>?,
    bridge: RequestBridge,
    sender: User,
    val guild: Guild
) : SlashCommandContext(bridge, sender)