package net.perfectdreams.discordinteraktions.common.entities

import net.perfectdreams.discordinteraktions.api.entities.Snowflake
import net.perfectdreams.discordinteraktions.common.utils.InteractionMessage

class DummyMessage : Message {
    override val id: Snowflake
        get() = TODO("Not yet implemented")

    override val content: String
        get() = TODO("Not yet implemented")
}