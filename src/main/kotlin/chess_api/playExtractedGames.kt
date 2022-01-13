package chess_api

import domain.game.makeMoves
import domain.move.IllegalMoveException
import java.io.File


/**
 * Plays extracted games, catching [IllegalMoveException]s.
 *
 * If an [IllegalMoveException] is caught, prints to the console that that move in the game was illegal.
 *
 * At the end of playing all games, returns the percentage of successfully played games.
 * @return percentage of successfully played games
 */
fun MonthExtraction.playExtractedGames(): Float {
    var successfullyPlayedGames = 0

    games.forEachIndexed { gameNum, extractedGame ->
        try {
            if (extractedGame.movesToMake.isNotEmpty())
                extractedGame.game.makeMoves(extractedGame.movesToMake.split(" "))

            successfullyPlayedGames++
        } catch (err: IllegalMoveException) {
            println("Month ${month} - Move \"${err.move}\" in game ${gameNum + 1} considered illegal: ${err.message}")
        }
    }

    return successfullyPlayedGames.toFloat() / games.size * 100
}

/**
 * Plays extracted games.
 *
 * @return percentage of successfully played games
 */
fun PlayerExtraction.playExtractedGames() =
    monthExtractions.map { it.playExtractedGames() }.average()




fun main() {
    val grandMasters = getPlayersFromTitle("GM").take(1)

    println("\n---------------Extracting games---------------\n")

    val playerExtractions = grandMasters.map { player -> PlayerExtraction(player, Location.API) }

    println("\n----------------Playing games----------------\n")

    playerExtractions.forEach { playerExtraction ->
        println("Playing ${playerExtraction.totalGameCount} games from player ${playerExtraction.player}...")

        val initialTime = System.currentTimeMillis()
        val percentage = playerExtraction.playExtractedGames()
        val endTime = System.currentTimeMillis()

        println("Successfully played $percentage% of games from this player")
        println("Time elapsed: ${endTime - initialTime}")
    }
}
