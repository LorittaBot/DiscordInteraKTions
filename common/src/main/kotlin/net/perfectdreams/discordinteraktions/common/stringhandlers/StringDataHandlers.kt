package net.perfectdreams.discordinteraktions.common.stringhandlers

class StringDataHandlers {
    private val handlers = mutableListOf<StringDataHandler>()

    fun provide(data: StringData<*>): String {
        for (handler in handlers) {
            val providedString = handler.handle(data)
            println("Provided string result via $handler: $providedString")

            if (providedString != null)
                return providedString
        }
        error("I don't know how to handle a ${data::class}! Did you forget to register a handler?")
    }

    fun addHandler(handler: StringDataHandler) = handlers.add(handler)

    init {
        addHandler {
            if (it !is RawStringData)
                return@addHandler null

            return@addHandler it.value
        }
    }
}