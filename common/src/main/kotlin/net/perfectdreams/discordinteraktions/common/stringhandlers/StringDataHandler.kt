package net.perfectdreams.discordinteraktions.common.stringhandlers

fun interface StringDataHandler {
    /**
     * Provide a [String] from the [data].
     *
     * If the returned String is null, the provided will be skipped.
     */
    fun handle(data: StringData<*>): String?
}