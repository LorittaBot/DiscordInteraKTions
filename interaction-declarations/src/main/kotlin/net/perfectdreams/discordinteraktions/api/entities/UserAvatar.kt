package net.perfectdreams.discordinteraktions.api.entities

// Inspired by Kord
// https://github.com/kordlib/kord/blob/ce7f0a12e6b9267e2d13f7995a29c903e6d0edd8/core/src/main/kotlin/entity/User.kt#L85
class UserAvatar(val userId: Long, val discriminator: Int, val avatarId: String?) {
    /**
     * The default avatar url for this user. Discord uses this for users who don't have a custom avatar set.
     */
    val defaultUrl: String get() = "https://cdn.discordapp.com/embed/avatars/${discriminator.toInt() % 5}.png"

    /**
     * Whether the user has an animated avatar.
     */
    val isAnimated: Boolean get() = avatarId?.startsWith("a_") ?: false

    /**
     * The supported format for this avatar
     */
    val format = when {
        isAnimated -> ImageFormat.GIF
        else -> ImageFormat.PNG
    }

    /**
     * The extension of the file for this avatar
     */
    val formatExtension = format.extension

    val url = "https://cdn.discordapp.com/avatars/userId/$avatarId.$formatExtension"

    enum class ImageFormat {
        PNG,
        GIF;

        val extension = this.name.toLowerCase()
    }
}