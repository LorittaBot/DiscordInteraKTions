import net.perfectdreams.discordinteraktions.InteractionsServer

suspend fun main() {
    val interactions = InteractionsServer(
        applicationId = 744361365724069898L,
        publicKey = "6410ed4a37c14c7fa5c5ea16ba8743cd40981f5f44a6dea2ddb2d5a658c8eead",
        token = "NzQ0MzYxMzY1NzI0MDY5ODk4.XziGiw.OXvJn3aVQtxAIF1YMOYW7uSkaUI"
    )

    interactions.commandManager.registerCommand(LorittaTestCommand())
    interactions.commandManager.registerCommand(MultiCommand())
    interactions.commandManager.registerCommand(EphemeralCommand())
    interactions.commandManager.updateAllCommands()

    interactions.start()
}