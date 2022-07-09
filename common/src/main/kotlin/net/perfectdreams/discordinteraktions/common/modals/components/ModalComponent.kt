package net.perfectdreams.discordinteraktions.common.modals.components

import dev.kord.common.entity.TextInputStyle

sealed class ModalComponent<T>(
    val label: String,
    val customId: String,
    val style: TextInputStyle
) : ModalComponentBuilder {
    override var allowedLength: ClosedRange<Int>? = null
    override var placeholder: String? = null
    override var value: String? = null
    override var required: Boolean? = null
    override var actionRowNumber: Int = 0
}

// ===[ STRING ]===
class StringModalComponent<T : String?>(
    label: String,
    customId: String,
    style: TextInputStyle
) : ModalComponent<T>(label, customId, style), StringModalComponentBuilder