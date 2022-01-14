package net.perfectdreams.discordinteraktions.declarations.commands.slash.options

import net.perfectdreams.discordinteraktions.common.autocomplete.AutocompleteExecutorDeclaration

class CommandOption<T>(
    // We need to store the command option type due to type erasure
    val type: CommandOptionType,
    val name: String,
    val description: String,
    val choices: List<CommandChoice<T>>,
    val autoCompleteExecutorDeclaration: AutocompleteExecutorDeclaration<T>?
)