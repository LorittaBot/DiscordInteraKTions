package net.perfectdreams.discordinteraktions.common.utils

import java.util.*

fun button(style: ButtonStyle, label: String, stateUniqueId: UUID): ButtonComponent {
    // Check if the executor exists
    return ButtonComponent(
        style = style,
        label = label,
        customId = stateUniqueId.toString()
    )
}

fun disabledButton(style: ButtonStyle, label: String) = ButtonComponent(
    style = style,
    label = label,
    disabled = true
)

fun urlButton(label: String, url: String, disabled: Boolean = false) = ButtonComponent(
    style = ButtonStyle.LINK,
    label = label,
    url = url,
    disabled = disabled
)