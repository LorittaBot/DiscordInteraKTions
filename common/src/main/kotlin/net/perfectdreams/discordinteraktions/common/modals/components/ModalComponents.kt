package net.perfectdreams.discordinteraktions.common.modals.components

open class ModalComponents {
    val arguments = mutableListOf<ModalComponent<*>>()

    fun textInput(customId: String, block: StringModalComponentBuilder.() -> (Unit) = {}) =
        StringModalComponent(customId).apply(block).also {
            it.register()
        }

    private fun <T> ModalComponent<T>.register(): ModalComponent<T> {
        if (arguments.any { it.customId == this.customId })
            throw IllegalArgumentException("Duplicate argument \"${this.customId}\"!")

        arguments.add(this)
        return this
    }
}