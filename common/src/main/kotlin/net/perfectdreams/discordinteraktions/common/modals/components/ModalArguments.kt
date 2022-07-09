package net.perfectdreams.discordinteraktions.common.modals.components

class ModalArguments(val types: Map<ModalComponent<*>, Any?>) {
    operator fun <T> get(argument: ModalComponent<T>): T {
        if (!types.containsKey(argument))
            throw RuntimeException("Missing argument ${argument.customId}!")

        return types[argument] as T
    }
}