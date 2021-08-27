package net.perfectdreams.discordinteraktions.api.entities

interface Member {
    val user: User
    val roles: List<Snowflake>
}
