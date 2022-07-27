package net.perfectdreams.discordinteraktions.common.modals.components

import dev.kord.common.entity.TextInputStyle
import net.perfectdreams.discordinteraktions.common.commands.options.OptionReference
import net.perfectdreams.discordinteraktions.common.commands.options.InteraKTionsCommandOption

open class ModalComponents {
    val registeredComponents = mutableListOf<InteraKTionsModalComponent<*>>()
    val references = mutableListOf<ComponentReference<*>>()

    fun textInput(customId: String, style: TextInputStyle, block: TextInputModalComponentBehaviorBuilder.() -> (Unit) = {}) =
        TextInputModalComponentBehaviorBuilder(customId, style)
            .apply(block)
            .let {
                register(it)
            }

    fun optionalTextInput(customId: String, style: TextInputStyle, block: NullableTextInputModalComponentBehaviorBuilder.() -> (Unit) = {}) =
        NullableTextInputModalComponentBehaviorBuilder(customId, style)
            .apply(block)
            .let {
                register(it)
            }
}


/**
 * Registers a [componentBuilder] to an [ModalComponents]
 *
 * @param componentBuilder the option builder
 * @return an [OptionReference]
 */
inline fun <reified T, ChoiceableType> ModalComponents.register(componentBuilder: ModalComponentBehaviorBuilder<T, ChoiceableType>): ComponentReference<T> {
    if (registeredComponents.any { it.customId == componentBuilder.id })
        throw IllegalArgumentException("Duplicate argument \"${componentBuilder.id}\"!")

    val componentReference = ComponentReference<T>(this, componentBuilder.id, componentBuilder.required)
    registeredComponents.add(componentBuilder.build())
    references.add(componentReference)

    return componentReference
}