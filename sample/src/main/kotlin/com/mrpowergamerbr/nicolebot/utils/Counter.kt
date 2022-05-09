package com.mrpowergamerbr.nicolebot.utils

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

// We will use a counter stored in memory instead of storing the data in the button itself because we need an way to synchronize the
// count!
//
// Because if multiple people clicked on the button at the same time, you wouldn't be able to really find out what is the *correct* counter
// value.
class Counter(
    var count: Int
) {
    private val mutex = Mutex()

    suspend fun get() = mutex.withLock {
        count
    }

    suspend fun addAndGet() = mutex.withLock {
        ++count
    }
}