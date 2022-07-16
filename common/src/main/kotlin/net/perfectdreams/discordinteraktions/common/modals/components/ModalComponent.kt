package net.perfectdreams.discordinteraktions.common.modals.components

sealed class ModalComponent<T>(
    val customId: String,
) {
    var required: Boolean = true
}

// ===[ STRING ]===
class TextInputModalComponent<T : String?>(
    customId: String
) : ModalComponent<T>(customId), StringModalComponentBuilder