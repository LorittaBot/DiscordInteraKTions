package com.mrpowergamerbr.nicolebot.commands

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
        val textOptionValue = context.getArgument(AutocompleteFunExecutor.options.text)

        // Description -> Value
        // You can send 25 autocomplete fields
        return mapOf(
            "You typed $filledValue" to filledValue,
            "You typed $textOptionValue" to textOptionValue,
            "You typed ${textOptionValue.reversed()}" to textOptionValue.reversed()
        )
    }
}