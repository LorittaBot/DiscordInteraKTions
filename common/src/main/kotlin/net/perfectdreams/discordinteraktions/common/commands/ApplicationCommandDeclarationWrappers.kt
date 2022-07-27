package net.perfectdreams.discordinteraktions.common.commands

interface ApplicationCommandDeclarationWrapper

interface SlashCommandDeclarationWrapper : ApplicationCommandDeclarationWrapper {
    fun declaration(): SlashCommandDeclarationBuilder
}

interface UserCommandDeclarationWrapper : ApplicationCommandDeclarationWrapper {
    fun declaration(): UserCommandDeclarationBuilder
}

interface MessageCommandDeclarationWrapper : ApplicationCommandDeclarationWrapper {
    fun declaration(): MessageCommandDeclarationBuilder
}