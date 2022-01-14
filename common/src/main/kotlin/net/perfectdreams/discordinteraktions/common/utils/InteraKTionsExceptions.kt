package net.perfectdreams.discordinteraktions.common.utils

object InteraKTionsExceptions {
    fun missingDeclaration(type: String): Nothing = error("The %s declaration wasn't found! Did you register the %s declaration?".format(type, type))
    fun missingExecutor(type: String): Nothing = error("The %s executor wasn't found! Did you register the %s executor?".format(type, type))
}