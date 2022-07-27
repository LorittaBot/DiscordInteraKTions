package net.perfectdreams.discordinteraktions.common.modals.components

import dev.kord.common.entity.TextInputStyle

abstract class ModalComponentBehaviorBuilder<T, ChoiceableType>(
    val id: String,
    val required: Boolean
) {
    abstract fun build(): InteraKTionsModalComponent<*>
}

// ===[ TEXT INPUT ]===
abstract class TextInputModalComponentBehaviorBuilderBase<T>(
    id: String,
    required: Boolean,
    val style: TextInputStyle
) : ModalComponentBehaviorBuilder<T, String>(id, required) {
    var minLength: Int? = null
    var maxLength: Int? = null
    var allowedLength: IntRange
        get() = error("This is a settable property only")
        set(value) {
            minLength = value.first
            maxLength = value.last
        }

    override fun build() = TextInputModalComponent(
        id,
        required,
        style,
        minLength,
        maxLength
    )
}

class TextInputModalComponentBehaviorBuilder(
    id: String,
    style: TextInputStyle
) : TextInputModalComponentBehaviorBuilderBase<String>(id, true, style)

class NullableTextInputModalComponentBehaviorBuilder(
    id: String,
    style: TextInputStyle
) : TextInputModalComponentBehaviorBuilderBase<String?>(id, false, style)