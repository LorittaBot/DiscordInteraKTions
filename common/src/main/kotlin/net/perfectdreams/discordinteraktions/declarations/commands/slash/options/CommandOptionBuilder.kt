package net.perfectdreams.discordinteraktions.declarations.commands.slash.options

import net.perfectdreams.discordinteraktions.common.autocomplete.AutocompleteExecutorDeclaration

open class CommandOptionBuilder<T>(
    // We need to store the command option type due to type erasure
    val type: CommandOptionType,
    val name: String,
    val description: String,
    val choices: MutableList<CommandChoice<T>>,
    var autocompleteExecutorDeclaration: AutocompleteExecutorDeclaration<T>?
) {
    fun choice(value: T, name: String): CommandOptionBuilder<T> {
        if (this.autocompleteExecutorDeclaration != null)
            error("You can't use pre-defined choices with an autocomplete executor set!")

        choices.add(
            CommandChoice(
                type,
                name,
                value
            )
        )
        return this
    }

    fun autocomplete(autocompleteExecutorDeclaration: AutocompleteExecutorDeclaration<T>): CommandOptionBuilder<T> {
        if (this.choices.isNotEmpty())
            error("You can't use an autocomplete executor with pre-defined choices set!")

        this.autocompleteExecutorDeclaration = autocompleteExecutorDeclaration

        return this
    }
}