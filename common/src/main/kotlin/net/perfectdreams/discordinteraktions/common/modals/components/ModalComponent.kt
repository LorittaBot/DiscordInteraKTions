package net.perfectdreams.discordinteraktions.common.modals.components

sealed class ModalComponent<T>(
    val customId: String,
)

// ===[ STRING ]===
class StringModalComponent(
    customId: String
) : ModalComponent<String>(customId), StringModalComponentBuilder