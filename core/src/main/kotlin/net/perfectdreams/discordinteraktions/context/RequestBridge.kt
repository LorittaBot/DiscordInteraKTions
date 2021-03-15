package net.perfectdreams.discordinteraktions.context

import net.perfectdreams.discordinteraktions.utils.Observable

class RequestBridge(
    val state: Observable<InteractionRequestState>
) {
    var manager: RequestManager = NoopRequestManager(this)
}