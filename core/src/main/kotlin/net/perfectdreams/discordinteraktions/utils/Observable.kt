package net.perfectdreams.discordinteraktions.utils

import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

class Observable<T>(value: T) {
    var value: T = value
        set(value) {
            field = value
            val currentListeners = listeners.toList()
            listeners.clear()
            currentListeners.forEach {
                it.resume(value)
            }
        }

    private val listeners = mutableListOf<Continuation<T>>()

    suspend fun awaitChange(): T {
        return suspendCancellableCoroutine {
            listeners.add(it)
        }
    }
}