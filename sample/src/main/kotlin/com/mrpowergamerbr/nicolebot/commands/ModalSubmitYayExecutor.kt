package com.mrpowergamerbr.nicolebot.commands

import net.perfectdreams.discordinteraktions.common.builder.message.embed
import net.perfectdreams.discordinteraktions.common.modals.ModalSubmitContext
import net.perfectdreams.discordinteraktions.common.modals.ModalSubmitExecutor
import net.perfectdreams.discordinteraktions.common.modals.ModalSubmitExecutorDeclaration
import net.perfectdreams.discordinteraktions.common.modals.components.ModalArguments
import net.perfectdreams.discordinteraktions.common.modals.components.ModalComponents

class ModalSubmitYayExecutor : ModalSubmitExecutor {
    companion object : ModalSubmitExecutorDeclaration("modal_submit_example") {
        object Options : ModalComponents() {
            val something = textInput("something")

            val somethingEvenBigger = optionalTextInput("something_even_bigger")
        }

        override val options = Options
    }

    override suspend fun onModalSubmit(
        context: ModalSubmitContext,
        args: ModalArguments
    ) {
        context.sendEphemeralMessage {
            content = "Done! You typed: ${args[Options.something]}"

            val somethingEvenBigger = args[Options.somethingEvenBigger]

            if (somethingEvenBigger != null) {
                embed {
                    title = "How's your day?"
                    description = args[Options.somethingEvenBigger]
                }
            }
        }
    }
}