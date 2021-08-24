package net.perfectdreams.discordinteraktions.common.components.buttons

open class ButtonClickExecutorDeclaration(
    /**
     * The "parent" is Any to avoid issues with anonymous classes
     *
     * When using anonymous classes, you can use another type to match declarations
     */
    val parent: Any,

    /**
     * The executor's ID, this is stored in the button, to be able to figure out what executor should be used
     *
     * All button executors should be unique!
     */
    val id: String
) {
    companion object {
        val ID_REGEX = Regex("[A-z0-9]+")
    }

    init {
        require(ID_REGEX.matches(id)) { "ID must respect the $ID_REGEX regular expression!" }
    }
}