package net.perfectdreams.discordinteraktions.common.modals.components

import dev.kord.common.entity.TextInputStyle

open class ModalComponents {
    val arguments = mutableListOf<ModalComponent<*>>()

    fun textInput(
        label: String,
        customId: String,
        style: TextInputStyle,
        builder: StringModalComponentBuilder.() -> (Unit) = {}
    ) = StringModalComponent<String>(label, customId, style).apply(builder).also {
        it.register()
    }

    private fun <T> ModalComponent<T>.register(): ModalComponent<T> {
        if (arguments.any { it.customId == this.customId })
            throw IllegalArgumentException("Duplicate argument \"${this.customId}\"!")

        arguments.add(this)
        return this
    }
}