package net.perfectdreams.discordinteraktions.platform.jda.utils

import kotlinx.coroutines.future.await
import net.dv8tion.jda.api.requests.RestAction

internal suspend fun <T> RestAction<T>.await() : T = this.submit().await()