package com.mrpowergamerbr.nicolebot.commands

import net.perfectdreams.discordinteraktions.common.builder.message.embed
import net.perfectdreams.discordinteraktions.common.modals.ModalSubmitContext
import net.perfectdreams.discordinteraktions.common.modals.ModalSubmitExecutor
import net.perfectdreams.discordinteraktions.common.modals.ModalSubmitExecutorDeclaration
import net.perfectdreams.discordinteraktions.common.modals.components.ModalArguments
import net.perfectdreams.discordinteraktions.common.modals.components.ModalComponents

class ModalSubmitYayExecutor : ModalSubmitExecutor {
    companion object : ModalSubmitExecutorDeclaration(ModalSubmitYayExecutor::class, "modal_submit_example") {
        object Options : ModalComponents() {
            val something = textInput("something")
                .register()

            val somethingEvenBigger = textInput("something_even_bigger")
                .register()
        }

        override val options = Options
    }

    override suspend fun onModalSubmit(
        context: ModalSubmitContext,
        modalArguments: ModalArguments
    ) {
        context.sendEphemeralMessage {
            content = "Done! You typed: ${modalArguments[Options.something]}"

            embed {
                title = "How's your day?"
                description = modalArguments[Options.somethingEvenBigger]
            }
        }
    }
}