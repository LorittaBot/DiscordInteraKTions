package com.mrpowergamerbr.nicolebot.commands.declarations

import dev.kord.common.entity.CommandArgument
import net.perfectdreams.discordinteraktions.common.autocomplete.AutocompleteContext
import net.perfectdreams.discordinteraktions.common.autocomplete.FocusedCommandOption
import net.perfectdreams.discordinteraktions.common.autocomplete.StringAutocompleteExecutor
import net.perfectdreams.discordinteraktions.common.autocomplete.StringAutocompleteExecutorDeclaration

class AutocompleteFunAutocompleteExecutor : StringAutocompleteExecutor {
    companion object : StringAutocompleteExecutorDeclaration(AutocompleteFunAutocompleteExecutor::class)

    override suspend fun onAutocomplete(
        context: AutocompleteContext,
        focusedOption: FocusedCommandOption
    ): Map<String, String> {
        val filledValue = focusedOption.value

        // We can get the value that the user has already filled in other options
        val textOption = context.arguments.first { it.name == "text" } as CommandArgument<String>
        val text = textOption.value

        // Description -> Value
        // You can send 25 autocomplete fields
        return mapOf(
            "You typed $filledValue" to filledValue,
            "You typed $text" to text,
            "You typed ${text.reversed()}" to text.reversed()
        )
    }
}