package net.perfectdreams.discordinteraktions.common.components

sealed class ComponentExecutorDeclaration(
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
    val id: String,

    /**
     * The RegEx that the [id] will be validated against
     *
     * By default, it will be validated against the [ID_REGEX] regex, but you can use another RegEx if you want to
     *
     * Keep in mind that some IDs may break Discord InteraKTions functionality, such as IDs containing ":"
     */
    idRegex: Regex = ID_REGEX
) {
    companion object {
        val ID_REGEX = Regex("[A-z0-9]+")
    }

    init {
        require(idRegex.matches(id)) { "ID must respect the $ID_REGEX regular expression!" }
    }
}

open class ButtonClickExecutorDeclaration(
    parent: Any,
    id: String
) : ComponentExecutorDeclaration(parent, id)

open class SelectMenuExecutorDeclaration(
    parent: Any,
    id: String
) : ComponentExecutorDeclaration(parent, id)