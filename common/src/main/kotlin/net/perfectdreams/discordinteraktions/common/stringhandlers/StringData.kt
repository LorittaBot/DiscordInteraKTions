package net.perfectdreams.discordinteraktions.common.stringhandlers

abstract class StringData<HelperObject> {
    abstract fun provide(obj: HelperObject): String
}