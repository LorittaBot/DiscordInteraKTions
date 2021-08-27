package net.perfectdreams.discordinteraktions.common.components

import net.perfectdreams.discordinteraktions.common.components.actionrow.ActionRowBuilder
import net.perfectdreams.discordinteraktions.common.components.buttons.ButtonClickExecutorDeclaration
import net.perfectdreams.discordinteraktions.common.components.buttons.ButtonStyle
import net.perfectdreams.discordinteraktions.common.utils.ActionRowComponent
import net.perfectdreams.discordinteraktions.common.utils.ButtonComponent
import net.perfectdreams.discordinteraktions.common.utils.InteraKTionsDslMarker

@InteraKTionsDslMarker
class ComponentsBuilder {
    val components = mutableListOf<ActionRowComponent>()

    fun actionRow(block: ActionRowBuilder.() -> Unit) {
        components.add(
            ActionRowBuilder()
                .apply(block)
                .build()
        )
    }

    fun build(): List<ActionRowComponent> {
        return components
    }
}