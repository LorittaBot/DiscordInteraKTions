package net.perfectdreams.discordinteraktions.common.entities

// Inspired by Kord
// https://github.com/kordlib/kord/blob/ce7f0a12e6b9267e2d13f7995a29c903e6d0edd8/core/src/main/kotlin/entity/User.kt#L85
class UserAvatar(val userId: ULong, val discriminator: Int, val avatarId: String?) {
    /**
     * The default avatar url for this user. Discord uses this for users who don't have a custom avatar set.
     */
    val defaultUrl: String get() = "https://cdn.discordapp.com/embed/avatars/${discriminator.toInt() % 5}.png"

    /**
     * Whether the user has set their avatar.
     */
    val isCustom: Boolean get() = avatarId != null

    /**
     * Whether the user has an animated avatar.
     */
    val isAnimated: Boolean get() = avatarId?.startsWith("a_") ?: false

    /**
     * The supported format for this avatar
     */
    val format: ImageFormat
        get() = when {
            isAnimated -> ImageFormat.GIF
            else -> ImageFormat.PNG
        }

    /**
     * The extension of the file for this avatar
     */
    val formatExtension = format.extension

    /**
     * Gets the avatar url in a supported format (defined by [format]) and default size.
     */
    val url: String get() = if (isCustom) "https://cdn.discordapp.com/avatars/$userId/$avatarId.$formatExtension" else defaultUrl

    enum class ImageFormat {
        PNG,
        GIF;

        val extension = this.name.toLowerCase()
    }
}