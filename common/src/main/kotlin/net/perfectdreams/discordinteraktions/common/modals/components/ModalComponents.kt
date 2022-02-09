package net.perfectdreams.discordinteraktions.common.modals.components

open class ModalComponents {
    val arguments = mutableListOf<ModalComponent<*>>()

    fun textInput(id: String) = StringModalComponentBuilder(id)

    fun <T> ModalComponentBuilder<T>.register(): ModalComponent<T> {
        if (arguments.any { it.name == this.name })
            throw IllegalArgumentException("Duplicate argument \"${this.name}\"!")

        val option = this.build()

        arguments.add(option)
        return option
    }
}