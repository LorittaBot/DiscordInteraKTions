package net.perfectdreams.discordinteraktions.common.commands

interface ApplicationCommandDeclarationWrapper {
    fun declaration(): ApplicationCommandDeclaration
}

interface SlashCommandDeclarationWrapper : ApplicationCommandDeclarationWrapper {
    override fun declaration(): SlashCommandDeclaration
}

interface UserCommandDeclarationWrapper : ApplicationCommandDeclarationWrapper{
    override fun declaration(): UserCommandDeclaration
}

interface MessageCommandDeclarationWrapper : ApplicationCommandDeclarationWrapper{
    override fun declaration(): MessageCommandDeclaration
}