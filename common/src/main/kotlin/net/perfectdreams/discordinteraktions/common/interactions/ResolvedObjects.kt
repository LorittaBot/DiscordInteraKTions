package net.perfectdreams.discordinteraktions.common.interactions

import net.perfectdreams.discordinteraktions.api.entities.Member
import net.perfectdreams.discordinteraktions.api.entities.Snowflake
import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.common.entities.messages.Message

// TODO: channels
// TODO: roles
class ResolvedObjects(
    val users: Map<Snowflake, User>?,
    val members: Map<Snowflake, Member>?,
    val messages: Map<Snowflake, Message>?
)