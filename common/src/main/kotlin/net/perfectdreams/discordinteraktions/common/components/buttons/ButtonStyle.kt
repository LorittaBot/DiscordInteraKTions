package net.perfectdreams.discordinteraktions.common.components.buttons

sealed class ButtonStyle(val value: Int) {
    // From Kord
    class Unknown(value: Int) : ButtonStyle(value)
    object Primary : ButtonStyle(1)
    object Secondary : ButtonStyle(2)
    object Success : ButtonStyle(3)
    object Danger : ButtonStyle(4)
    object Link : ButtonStyle(5)
}