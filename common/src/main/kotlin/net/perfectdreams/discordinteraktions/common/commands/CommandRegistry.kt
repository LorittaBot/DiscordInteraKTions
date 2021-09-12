package net.perfectdreams.discordinteraktions.common.commands

import dev.kord.common.entity.Snowflake

interface CommandRegistry {
    suspend fun updateAllCommandsInGuild(guildId: Snowflake, deleteUnknownCommands: Boolean)
    suspend fun updateAllGlobalCommands(deleteUnknownCommands: Boolean)
}