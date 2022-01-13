package chess_api

import domain.game.gameFromMoves
import java.io.File
import java.io.PrintWriter

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
fun extractGames(player: String, month: String, location: Location, logger: PrintWriter): List<ExtractedGame> {
    val fileName = "$CHESS_API_FOLDER_LOCATION/${location} files/ChessCom_${player}_$month.${location}"
    val file = File(fileName)

    if (location != Location.API)
        require(file.exists()) { "File $fileName does not exist. Check the player and month or download the file." }

    val extractingMessage = "Extracting games from $player in ${month.substring(4..5)}/${month.substring(0..3)}..."
    print(extractingMessage)
    logger.println(extractingMessage)

    val games = when (location) {
        Location.API -> extractGamesFromPGN(
            getMonthPGNList("$CHESS_COM_API_URL/pub/player/erik/games/${month.substring(0..3)}/${month.substring(4..5)}"),
            logger
        )
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
fun extractGamesFromJSON(lines: List<String>, logger: PrintWriter): List<ExtractedGame> {
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
fun extractGamesFromPGN(pgnList: List<PGN>, logger: PrintWriter): List<ExtractedGame> {
    return pgnList
        .filterIndexed { idx, pgn ->
            if (pgn["Variant"] != null) {
                logger.println("Ignored game ${idx + 1} because it is of a different game variant.")
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
