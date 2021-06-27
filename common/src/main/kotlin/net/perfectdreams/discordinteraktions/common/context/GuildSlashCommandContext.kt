package net.perfectdreams.discordinteraktions.common.context

import net.perfectdreams.discordinteraktions.api.entities.Guild

open class GuildSlashCommandContext(
    // TODO:
    // val request: CommandInteraction,
    // val relativeOptions: List<Option>?,
    val guild: Guild,
    bridge: RequestBridge
) : SlashCommandContext(bridge)