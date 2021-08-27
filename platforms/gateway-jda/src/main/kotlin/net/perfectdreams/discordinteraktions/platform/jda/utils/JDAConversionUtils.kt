package net.perfectdreams.discordinteraktions.platform.jda.utils

import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.Button
import net.dv8tion.jda.api.interactions.components.ButtonStyle
import net.perfectdreams.discordinteraktions.common.utils.ActionRowComponent
import net.perfectdreams.discordinteraktions.common.utils.ButtonComponent

/**
 * Has utils functions that helps to convert Discord InteraKTion classes to JDA classes
 */
object JDAConversionUtils {
    fun convertButtonComponent(component: ButtonComponent) = Button.of(
        ButtonStyle.fromKey(component.style.id),
        component.url ?: component.customId ?: "dummy",
        component.label,
        null
    ).withDisabled(component.disabled)

    fun convertActionRowComponent(component: ActionRowComponent) = ActionRow.of(
        component.components.map {
            if (it is ButtonComponent)
                convertButtonComponent(it)
            else
                error("Unsupported component within ActionRow $it")
        }
    )
}