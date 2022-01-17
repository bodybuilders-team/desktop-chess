package play_games_from_chess_com

import domain.game.gameFromFEN
import domain.game.gameFromMoves
import java.io.File
import java.io.PrintWriter


const val PLAY_GAMES_FROM_CHESS_API_FOLDER_LOCATION = "src/main/kotlin/play_games_from_chess_com"
const val CHESS_EXTRACTED_FOLDER_LOCATION = "$PLAY_GAMES_FROM_CHESS_API_FOLDER_LOCATION/extracted"

/**
 * Type of the location where the games will be extracted from.
 */
enum class Location {
    API, JSON, PGN;

    override fun toString() = name.lowercase()
}

data class Month(val month: String, val year: String) {

    fun toString(monthFirst: Boolean, separatingString: String) =
        "${if (monthFirst) month else year}$separatingString${if (monthFirst) year else month}"

    override fun toString() = toString(monthFirst = false, separatingString = "")
}


/**
 * Extract games from a player in a given month, given the location to get data from (API, JSON or PGN).
 *
 * Exact same games will be extracted regardless of location.
 * @param player player to get games from
 * @param month month to get games from
 * @param location type of location to get data from
 * @param logger logger to log information to
 * @return extracted games
 */
fun extractGames(player: String, month: Month, location: Location, logger: PrintWriter?): List<ExtractedGame> {
    val fileName = "$PLAY_GAMES_FROM_CHESS_API_FOLDER_LOCATION/${location} files/ChessCom_${player}_$month.${location}"
    val file = File(fileName)

    if (location != Location.API)
        require(file.exists()) { "File $fileName does not exist. Check the player and month or download the file." }

    val extractingMessage =
        "Extracting games from $player in ${month.toString(monthFirst = true, separatingString = "/")}..."
    print(extractingMessage)
    logger?.println(extractingMessage)

    val games = when (location) {
        Location.API -> extractGamesFromPGN(getMonthPGNList(player, month), logger)
        Location.JSON -> extractGamesFromJSON(file.readLines(), logger)
        Location.PGN -> TODO("") //extractGamesFromJSON(file.readLines(), logger) //extractGamesFromPGN(file.readLines(), logger)
    }

    println(" - Extracted ${games.size} games")

    return games
}


/**
 * Extract games from a player in a given month, given a list of string [lines] in JSON format.
 *
 * Writes to a logger information on what games where ignored and for what reason.
 *
 * @param lines list of string in JSON format
 * @param logger logger to log information to
 * @return extracted games
 */
fun extractGamesFromJSON(lines: List<String>, logger: PrintWriter?): List<ExtractedGame> {
    val pgnList = getPGNListFromJSON(lines.joinToString("") { it.replace("\n", "") })

    return extractGamesFromPGN(pgnList, logger)
}


/**
 * Extract games from a player in a given month, given a list of PGN [pgnList].
 *
 * Writes to a logger information on what games where ignored and for what reason.
 *
 *
 * Removes information from moves:
 * - comments (timestamp included)
 * - turn number
 * - check character '+'
 * - checkmate character '#'
 * - who won
 *
 * @param pgnList list of PGN objects
 * @param logger logger to log information to
 * @return extracted games
 */
fun extractGamesFromPGN(pgnList: List<PGN>, logger: PrintWriter?): List<ExtractedGame> {
    return pgnList
        .filterIndexed { idx, pgn ->
            if (pgn["Variant"] != null) {
                logger?.println("Ignored game ${idx + 1} because it is of a different game variant.")
            }

            pgn["Variant"] == null && pgn.moves.substringBefore(" ") == "1."
        }
        .map { pgn ->
            ExtractedGame(
                movesToMake = pgn.moves.replace(Regex("\\{.*?} "), "")
                    .replace(Regex("\\d+\\.(\\.\\.)? "), "")
                    .replace("+", "")
                    .replace("#", "")
                    .replace(Regex("( )?(0-1|1-0|1/2-1/2)"), ""),
                game = if (pgn["SetUp"] == "1") gameFromFEN(pgn["FEN"]!!) else gameFromMoves()
            )
        }
}
