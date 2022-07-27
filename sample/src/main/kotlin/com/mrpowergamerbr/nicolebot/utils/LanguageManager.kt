package com.mrpowergamerbr.nicolebot.utils

class LanguageManager {
    val strings = mapOf(
        "command_label" to "externallyprovidedstring",
        "command_description" to "You can provide strings dynamically, useful if you want to provide a i18n key!",
        "command_option" to "A nice and fluffy option",
        "command_choice_name" to "This is dynamically provided from the LanguageManager class!"
    )

    fun get(key: String) = strings[key]!!
}