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

data class PlayerExtraction(val player: String, val location: Location) {
    val monthExtractions: List<MonthExtraction> = extractAllGamesFromPlayer(player, location)
    val totalGameCount = monthExtractions.sumOf { monthExtraction ->
        monthExtraction.games.size
    }
}


/**
 * Extracts all games from player.
 *
 * @param player name of the player
 * @param location location where the games will be extracted from
 * @return [PlayerExtraction] containing the name of the player and all the monthExtractions.
 */
fun extractAllGamesFromPlayer(player: String, location: Location): List<MonthExtraction> {
    val months = getMonthlyArchives(player).map { it.substring((it.length - 7) until it.length).replace("/", "") }

    val logger = File("$CHESS_EXTRACTED_FOLDER_LOCATION/log/${player}.txt").printWriter()

    val monthExtractions =
        months.mapNotNull {
            try {
                MonthExtraction(player = player, month = it, location = location, logger = logger)
            } catch (exception: Exception) {
                println("Couldn't get that month's games!")
                null
            }
        }

    logger.close()

    writeToFile(monthExtractions)

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
