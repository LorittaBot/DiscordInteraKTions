package net.perfectdreams.discordinteraktions.common.utils

import net.perfectdreams.discordinteraktions.common.buttons.ButtonStateManager
import java.util.*

suspend fun <T> button(manager: ButtonStateManager, style: ButtonStyle, label: String, executorSignature: Any, data: T): ButtonComponent {
    // Check if the executor exists
    val executor = manager.buttonExecutors.firstOrNull {
        it.signature() == executorSignature
    } ?: error("There isn't any registered button executor that matches the signature $executorSignature! Did you register the executor?")

    val uniqueId = UUID.randomUUID()
    manager.storeState(uniqueId, executor.signature(), data!!)

    return ButtonComponent(
        style = style,
        label = label,
        customId = uniqueId.toString()
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