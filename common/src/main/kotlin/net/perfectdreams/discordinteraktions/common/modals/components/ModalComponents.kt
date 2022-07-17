package net.perfectdreams.discordinteraktions.common.modals.components

open class ModalComponents {
    val arguments = mutableListOf<ModalComponent<*>>()

    fun textInput(customId: String, block: StringModalComponentBuilder.() -> (Unit) = {}) =
        TextInputModalComponent<String>(customId).apply(block).also {
            it.register()
        }

    fun optionalTextInput(customId: String, block: StringModalComponentBuilder.() -> (Unit) = {}) =
        TextInputModalComponent<String?>(customId).apply(block).also {
            it.register()
        }

    private inline fun <reified T> ModalComponent<T>.register(): ModalComponent<T> {
        if (arguments.any { it.customId == this.customId })
            throw IllegalArgumentException("Duplicate argument \"${this.customId}\"!")

        this.apply {
            required = null !is T
        }

        arguments.add(this)
        return this
    }
}