package net.perfectdreams.discordinteraktions.common.context

enum class InteractionRequestState {
    /**
     * Not replied to the interaction request yet
     */
    NOT_REPLIED_YET,

    /**
     * Request was deferred without sending any messages
     */
    DEFERRED_CHANNEL_MESSAGE,

    /**
     * Request was deferred without sending any messages
     */
    DEFERRED_UPDATE_MESSAGE,

    /**
     * Replied to the interaction with a message
     */
    ALREADY_REPLIED
}