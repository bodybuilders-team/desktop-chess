package playGamesFromChessCom

import domain.game.Game
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.PrintWriter

// TODO - Allow different game variants (PGN header "Variant")

const val NUMBER_OF_CHARS_MONTH_YEAR = 7

/**
 * Extracted game, containing the initial board and the moves to make.
 */
data class ExtractedGame(val movesToMake: String, val game: Game)

/**
 * Data class containing player, month, and games of the player in that month.
 * @property player player to get games from
 * @property month month to get games from
 * @property location locations to get the games from
 * @property games the extracted games
 */
data class MonthExtraction(val player: String, val month: Month, val location: Location, val logger: PrintWriter?) {
    val games: List<ExtractedGame> = extractGames(player, month, location, logger)

    override fun toString() = "$player/$month/$location"
}

data class PlayerExtraction(val player: String, val location: Location, val log: Boolean, val storeGames: Boolean) {
    val monthExtractions: List<MonthExtraction> = extractAllGamesFromPlayer(player, location, log, storeGames)
    val totalGameCount = monthExtractions.sumOf { it.games.size }
}

/**
 * Extracts all games from player.
 *
 * @param player name of the player
 * @param location location where the games will be extracted from
 * @return [PlayerExtraction] containing the name of the player and all the monthExtractions.
 */
fun extractAllGamesFromPlayer(
    player: String,
    location: Location,
    log: Boolean,
    storeGames: Boolean
): List<MonthExtraction> {
    val months = getMonthlyArchives(player)
        .map {
            val (year, month) = it.takeLast(NUMBER_OF_CHARS_MONTH_YEAR).split("/")
            Month(month, year)
        }

    val logger = if (log) File("$CHESS_EXTRACTED_FOLDER_LOCATION/log/$player.txt").printWriter() else null

    val initTime = System.currentTimeMillis()

    print("Extracting all games from $player...")

    val monthExtractions = runBlocking(Dispatchers.Default) {
        coroutineScope {
            months.map {
                async {
                    try {
                        MonthExtraction(player = player, month = it, location = location, logger = logger)
                    } catch (exception: Exception) {
                        null
                    }
                }
            }.awaitAll().filterNotNull()
        }
    }

    val endTime = System.currentTimeMillis()

    println(" - Extracted ${monthExtractions.sumOf { it.games.size }} games in ${endTime - initTime}ms")

    logger?.close()

    if (storeGames) writeToFile(monthExtractions)

    return monthExtractions
}

/**
 * Writes the all [monthExtractions] to a file.
 * @param monthExtractions list of month extractions
 */
fun writeToFile(monthExtractions: List<MonthExtraction>) {
    val printWriter =
        File("$CHESS_EXTRACTED_FOLDER_LOCATION/games/${monthExtractions.first().player}.txt").printWriter()

    monthExtractions.forEach { monthExtraction ->
        monthExtraction.games.forEach { game ->
            printWriter.println(game.movesToMake)
        }
    }
    printWriter.close()
}
