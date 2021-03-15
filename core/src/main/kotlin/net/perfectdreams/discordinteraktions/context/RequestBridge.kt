package net.perfectdreams.discordinteraktions.context

import net.perfectdreams.discordinteraktions.utils.Observable

/**
 * Bridges code between a interaction handler and the request managers
 *
 * Used to track [state] changes and to allow request managers to switch to other request managers within their own code
 *
 * **Don't forget to initialize the [manager] after creating the bridge!**
 *
 * @param state a [Observable] interaction request state
 */
class RequestBridge(
    val state: Observable<InteractionRequestState>
) {
    var _manager: RequestManager? = null

    var manager: RequestManager
        get() = _manager ?: throw IllegalArgumentException("RequestManager is null!")
        set(value) {
            _manager = value
        }
}