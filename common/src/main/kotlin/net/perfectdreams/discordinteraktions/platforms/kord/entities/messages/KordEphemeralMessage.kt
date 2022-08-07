package net.perfectdreams.discordinteraktions.platforms.kord.entities.messages

import dev.kord.common.entity.DiscordMessage
import dev.kord.core.Kord

open class KordEphemeralMessage(kord: Kord, data: DiscordMessage) : KordMessage(kord, data)