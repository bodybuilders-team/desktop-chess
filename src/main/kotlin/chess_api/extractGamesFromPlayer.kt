package chess_api

import domain.game.*
import domain.board.Board
import domain.move.IllegalMoveException
import domain.move.Move
import java.io.File
import java.io.PrintWriter

//TODO - Allow different game variants (PGN header "Variant")


const val CHESS_API_FOLDER_LOCATION = "src/main/kotlin/chess_api"
const val CHESS_EXTRACTED_FOLDER_LOCATION = "$CHESS_API_FOLDER_LOCATION/extracted"


/**
 * Type of the location where the games will be extracted from.
 */
enum class Location {
    API, JSON, PGN;

    override fun toString() = name.lowercase()
}


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
data class MonthExtraction(val player: String, val month: String, val location: Location, val logger: PrintWriter) {
    val games: List<ExtractedGame> = extractGames(player, month, location, logger)

    override fun toString() = "${player}/${month}/${location}"
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
fun extractGames(player: String, month: String, location: Location, logger: PrintWriter): List<ExtractedGame> {
    val fileName = "$CHESS_API_FOLDER_LOCATION/${location} files/ChessCom_${player}_$month.${location}"
    val file = File(fileName)

    if (location != Location.API)
        require(file.exists()) { "File $fileName does not exist. Check the player and month or download the file." }

    val whatToExtractString = "games from $player in ${month.substring(4..5)}/${month.substring(0..3)}"

    println("Extracting $whatToExtractString...")
    logger.println("Extracting $whatToExtractString...")

    val games = when (location) {
        Location.API -> extractGamesFromPGN(
            getMonthPGNList("$CHESS_COM_API_URL/pub/player/erik/games/${month.substring(0..3)}/${month.substring(4..5)}"),
            logger
        )
        Location.JSON -> extractGamesFromJSON(file.readLines(), logger)
        Location.PGN -> TODO("") //extractGamesFromJSON(file.readLines(), logger) //extractGamesFromPGN(file.readLines(), logger)
    }

    println("Extracted ${games.size} $whatToExtractString.")

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


/**
 * Plays extracted games from a [monthExtraction], catching [IllegalMoveException]s.
 *
 * If an [IllegalMoveException] is caught, prints to the console that that move in the game was illegal.
 *
 * At the end of playing all games, prints to the console the percentage of successfully played games.
 * @param monthExtraction month extraction with the extracted games
 */
fun playExtractedGames(monthExtraction: MonthExtraction) {
    var successfullyPlayedGames = 0

    monthExtraction.games.forEachIndexed { gameNum, extractedGame ->
        try {
            if (extractedGame.movesToMake.isNotEmpty())
                extractedGame.game.makeMoves(extractedGame.movesToMake.split(" "))
            successfullyPlayedGames++
        } catch (err: IllegalMoveException) {
            println("Move \"${err.move}\" in game ${gameNum + 1} considered illegal: ${err.message}")
        }
    }

    monthExtraction.apply {
        val playerAndMonth = "$player - ${month.substring(4..5)}/${month.substring(0..3)}"
        val percentage = "${successfullyPlayedGames.toDouble() / games.size * 100}%"

        println("$playerAndMonth - $successfullyPlayedGames successfully played games ($percentage)")
    }
}


/**
 * Returns a game obtained from a FEN string (Forsyth-Edwards Notation).
 * @param fen FEN in string
 * @return game obtained from a FEN string
 */
fun gameFromFEN(fen: String): Game {
    // rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1

    val boardRepresentation = fen.substringBefore(" ")

    val boardInString = boardRepresentation.replace("/", "")
        .replace("8", " ".repeat(8))
        .replace("7", " ".repeat(7))
        .replace("6", " ".repeat(6))
        .replace("5", " ".repeat(5))
        .replace("4", " ".repeat(4))
        .replace("3", " ".repeat(3))
        .replace("2", " ".repeat(2))
        .replace("1", " ".repeat(1))

    val (armyToPlay, castlesAvailable,
        enPassantTarget, movesSinceCapture, totalMoves) = fen.substringAfter(" ").split(" ")

    val moves = mutableListOf<String>()

    if ('K' !in castlesAvailable) moves.add("Rg1f1")
    if ('Q' !in castlesAvailable) moves.add("Ra1b1")
    if ('k' !in castlesAvailable) moves.add("Rg1f1")
    if ('q' !in castlesAvailable) moves.add("Ra1b1")
    if (enPassantTarget != "-") {
        val enPassantCol = enPassantTarget[0]
        val enPassantRow = enPassantTarget[1]
        moves.add(
            "P$enPassantCol${if (enPassantRow == '6') "7" else "2"}$enPassantCol${if (enPassantRow == '6') "5" else "4"}"
        )
    }
    if ((moves.size % 2 == 0) != (armyToPlay == "w"))
        moves.add(0, "Pe2e4")

    return Game(Board(boardInString), moves.map { Move(it) })
}


fun main() {
    println("\n---------------Extracting games---------------\n")

    val location = Location.API
    val player = "erik"

    val months = getMonthlyArchives(player).map { it.substring((it.length - 7) until it.length).replace("/", "") }

    val logger = File("$CHESS_EXTRACTED_FOLDER_LOCATION/log/${player}.txt").printWriter()

    val monthExtractions =
        months.map { MonthExtraction(player = player, month = it, location = location, logger = logger) }

    logger.close()

    println("\n----------------Playing games----------------\n")

    writeToFile(monthExtractions)

    monthExtractions.forEach { monthExtraction ->
        playExtractedGames(monthExtraction)
    }
}
