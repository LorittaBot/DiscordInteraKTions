package com.mrpowergamerbr.nicolebot.utils

import net.perfectdreams.discordinteraktions.common.stringhandlers.StringData

class ExternalStringData(private val key: String) : StringData<LanguageManager>() {
    override fun provide(obj: LanguageManager) = obj.strings[key]!!
}