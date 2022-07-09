package net.perfectdreams.discordinteraktions.common.modals.components

sealed class ModalComponentBuilder<T>(
    val name: String
) {
    abstract fun build(): ModalComponent<T>
}

// ===[ STRING ]===
class StringModalComponentBuilder(name: String) : ModalComponentBuilder<String>(name) {
    override fun build() = StringModalComponent(
        name
    )
}