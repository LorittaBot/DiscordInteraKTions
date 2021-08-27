package net.perfectdreams.discordinteraktions.common.commands

import net.perfectdreams.discordinteraktions.api.entities.Snowflake

interface CommandRegistry {
    suspend fun updateAllCommandsInGuild(guildId: Snowflake, deleteUnknownCommands: Boolean)
    suspend fun updateAllGlobalCommands(deleteUnknownCommands: Boolean)
}