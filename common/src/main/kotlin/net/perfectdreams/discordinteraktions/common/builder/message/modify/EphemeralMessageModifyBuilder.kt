package net.perfectdreams.discordinteraktions.common.builder.message.modify

/**
 * Message builder for a message that does not persists between client reloads.
 */
interface EphemeralMessageModifyBuilder : MessageModifyBuilder {
    // We need to access the delegated stuff ourselves
    var state: MessageModifyStateHolder
}