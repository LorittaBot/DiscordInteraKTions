package net.perfectdreams.discordinteraktions.api.entities

inline class Snowflake(val value: Long) {
    val timestamp: Long
        get() = (value shr 22) + 1420070400000

    val internalWorkerId: Long
        get() = value and 0x3E0000 shr 17

    val internalProcessId: Long
        get() = value and 0x1F000 shr 12

    val increment: Long
        get() = value and 0xFFF
}