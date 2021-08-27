package net.perfectdreams.discordinteraktions.common.components.actionrow

import net.perfectdreams.discordinteraktions.common.components.buttons.ButtonClickExecutorDeclaration
import net.perfectdreams.discordinteraktions.common.components.buttons.ButtonStyle
import net.perfectdreams.discordinteraktions.common.utils.ActionRowComponent
import net.perfectdreams.discordinteraktions.common.utils.ButtonComponent
import net.perfectdreams.discordinteraktions.common.utils.InteraKTionsDslMarker

@InteraKTionsDslMarker
class ActionRowBuilder {
    val components = mutableListOf<ButtonComponent>()

    fun interactiveButton(
        style: ButtonStyle,
        label: String,
        executor: ButtonClickExecutorDeclaration,
        disabled: Boolean = false
    ) {
        require(style != ButtonStyle.Link) { "You cannot use a ButtonStyle.Link style in a interactive button! Please use \"linkButton(...)\" if you want to create a button with a link" }
        components.add(
            ButtonComponent(
                style = style,
                label = label,
                customId = executor.id
            )
        )
    }

    fun interactiveButton(
        style: ButtonStyle,
        label: String,
        executor: ButtonClickExecutorDeclaration,
        data: String,
        disabled: Boolean = false
    ) {
        components.add(
            ButtonComponent(
                style = style,
                label = label,
                customId = "${executor.id}:$data",
                disabled = disabled
            )
        )
    }

    fun linkButton(
        label: String,
        url: String,
        disabled: Boolean = false
    ) {
        components.add(
            ButtonComponent(
                style = ButtonStyle.Link,
                label = label,
                url = url,
                disabled = disabled
            )
        )
    }

    fun build(): ActionRowComponent {
        return ActionRowComponent(components)
    }
}