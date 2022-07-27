package net.perfectdreams.discordinteraktions.common.modals.components

class ModalArguments(val types: Map<ComponentReference<*>, Any?>) {
    operator fun <T> get(argument: ComponentReference<T>): T {
        if (!types.containsKey(argument) && argument.required)
            throw RuntimeException("Missing argument ${argument.customId}!")

        return types[argument] as T
    }
}