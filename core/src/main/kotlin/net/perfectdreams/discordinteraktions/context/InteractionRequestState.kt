package net.perfectdreams.discordinteraktions.context

enum class InteractionRequestState {
    /**
     * Not replied to the interaction request yet
     */
    NOT_REPLIED_YET,

    /**
     * Request was deferred without sending any messages
     */
    DEFERRED,

    /**
     * Replied to the interaction with a message
     */
    ALREADY_REPLIED
}