package com.mrpowergamerbr.nicolebot.commands

import dev.kord.common.entity.TextInputStyle
import net.perfectdreams.discordinteraktions.common.builder.message.embed
import net.perfectdreams.discordinteraktions.common.modals.ModalSubmitContext
import net.perfectdreams.discordinteraktions.common.modals.ModalSubmitExecutor
import net.perfectdreams.discordinteraktions.common.modals.ModalSubmitExecutorDeclaration
import net.perfectdreams.discordinteraktions.common.modals.components.ModalArguments
import net.perfectdreams.discordinteraktions.common.modals.components.ModalComponents

class ModalSubmitYayExecutor : ModalSubmitExecutor {
    companion object : ModalSubmitExecutorDeclaration("modal_submit_example") {
        object Options : ModalComponents() {
            val something = textInput("something", "something cool and epic!", TextInputStyle.Short) {
                actionRowNumber = 0
            }

            val somethingEvenBigger = textInput("something_even_bigger", "How's your day?", TextInputStyle.Paragraph) {
                actionRowNumber = 1

                required = false
                allowedLength = 50..200
            }
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

            // In Discord's API: Optional TextInputs are empty if the user hasn't filled it
            if (somethingEvenBigger.isNotEmpty()) {
                embed {
                    title = "How's your day?"
                    description = args[Options.somethingEvenBigger]
                }
            }
        }
    }
}