package net.perfectdreams.discordinteraktions.common.stringhandlers

class RawStringData(val value: String) : StringData<Unit>() {
    override fun provide(obj: Unit) = value
}