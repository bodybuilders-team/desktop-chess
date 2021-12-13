package chess_api

import domain.gameFromMoves
import domain.move.IllegalMoveException
import java.io.File
import java.io.PrintWriter

//TODO("Allow different game variants (initial board differs)
// Game variants Chess960")

//TODO("Allow obtaining partial games initial board through FEN notation setup")

//TODO("Make API calls to get JSON file containing all games of a player in a given month
// Example: https://api.chess.com/pub/player/erik/games/2009/10")


// https://www.chess.com/news/view/published-data-api#pubapi-general

// List of titled-player usernames.
// URL pattern: https://api.chess.com/pub/titled/{title-abbrev}
// Example: https://api.chess.com/pub/titled/GM

// Array of monthly archives available for this player.
// URL pattern: https://api.chess.com/pub/player/{username}/games/archives
// Example: https://api.chess.com/pub/player/erik/games/archives

// Downloads PGN file containing all games of a player in a given month.
// URL pattern: https://api.chess.com/pub/player/{username}/games/{YYYY}/{MM}/pgn
// Example: https://api.chess.com/pub/player/erik/games/2009/10/pgn
// Downloaded file name: ChessCom_username_YYYYMM.pgn


/**
 * Type of the file where the games will be extracted from.
 */
enum class FileType {
    JSON, PGN;

    override fun toString() = name.lowercase()
}


/**
 * Extract games from a player in a given month, given the filetype (JSON or PGN).
 *
 * Exact same games will be extracted regardless of filetype.
 * @param player player to get games from
 * @param month month to get games from
 * @param fileType type of the file where the games are
 * @return extracted games
 */
fun extractGamesFromPlayer(player: String, month: String, fileType: FileType): List<String> {
    val fileName = "src/main/kotlin/chess_api/${fileType} files/ChessCom_${player}_$month.${fileType}"
    val file = File(fileName)
    require(file.exists()) { "File $fileName does not exist. Check the player and month or download the file." }

    val logger =
        File("src/main/kotlin/chess_api/extracted games/log/ChessCom_${player}_$month.${fileType}.txt").printWriter()

    val whatToExtractString = "games from $player in ${month.substring(4..5)}/${month.substring(0..3)}"

    println("Extracting $whatToExtractString...")

    val games = when (fileType) {
        FileType.PGN -> extractGamesFromPGN(file.readLines(), logger)
        FileType.JSON -> extractGamesFromJSON(file.readLines(), logger)
    }

    logger.close()

    println("Extracted ${games.size} $whatToExtractString.")

    return games
}


/**
 * Extract games from a player in a given month, given a list of strings [lines] in JSON format.
 *
 * Writes to a logger information on what games where ignored and for what reason.
 *
 * @param lines list of string in JSON format
 * @param logger logger to log information to
 * @return extracted games
 */
fun extractGamesFromJSON(lines: List<String>, logger: PrintWriter): List<String> {
    val pgn = lines.filter { it.contains("pgn") }
        .map {
            it.substringAfter("\"pgn\": \"")
                .dropLast(2)
                .split("\\n")
        }

    return extractGamesFromPGN(pgn.flatten(), logger)
}


/**
 * Extract games from a player in a given month, given a list of strings [lines] in PGN format.
 *
 * Writes to a logger information on what games where ignored and for what reason.
 *
 * @param lines list of string in PGN format
 * @param logger logger to log information to
 * @return extracted games
 */
fun extractGamesFromPGN(lines: List<String>, logger: PrintWriter): List<String> {
    var gameCount = 0
    var extractedGameCount = 0
    var gameVariant = ""
    var ignoreNext = false

    val ignoredGamesFiltered = lines.filter { line ->
        val isGame = line.isNotBlank() && line.first().isDigit()

        if (line.startsWith("[Variant")) {
            gameVariant = line.substringAfter("\"").substringBefore("\"").replace("\\", "")
            ignoreNext = true
            false
        } else if (isGame) {
            gameCount++

            if (ignoreNext) logger.println("Ignored game $gameCount because of different game variant \"$gameVariant\".")

            (if (line.first() == '1') {
                if (!ignoreNext) extractedGameCount++
                !ignoreNext
            } else {
                logger.println("Ignored game $gameCount because it doesn't start on move 1.")
                false
            })
                .also { ignoreNext = false }
        } else false
    }

    logger.println("Extracted $extractedGameCount from $gameCount.")

    return ignoredGamesFiltered.map {
        it.replace(Regex("\\{\\[%clk 0:\\d\\d:\\d\\d(\\.\\d)?]} "), "") // time clock (clock game)
            .replace(Regex("\\d+\\.\\.\\. "), "")                       // turn number (clock game)
            .replace("+", "")                                           // check character '+'
            .replace("#", "")                                           // checkmate character '#'
            .replace(Regex("\\d+\\. "), "")                             // turn number
            .replace(Regex(" (0-1|1-0|1/2-1/2)"), "")                   // who won
    }.sorted()
}


/**
 * Data class containing player, month, and games of the player in that month.
 * @property player player to get games from
 * @property month month to get games from
 * @property fileType type of the file where the games are
 * @property games the extracted games
 */
data class MonthExtraction(val player: String, val month: String, val fileType: FileType) {
    val games: List<String> = extractGamesFromPlayer(player, month, fileType)

    override fun toString() = "ChessCom_${player}_${month}_${fileType}"
}


/**
 * Writes the games of a month, one in each line.
 * @param monthExtraction month extraction containing player, month, and games of the player in that month
 */
fun writeToFile(monthExtraction: MonthExtraction) {
    val printWriter = File("src/main/kotlin/chess_api/extracted games/${monthExtraction}.txt").printWriter()
    monthExtraction.games.forEach { game ->
        printWriter.println(game)
    }
    printWriter.close()
}


/**
 * Compares if extracted games are the same regardless of file type specified.
 * @param player player to get games from
 * @param month month to get games from
 * @return true if the extractions from the different file types are the same
 */
fun extractionsAreEqual(player: String, month: String): Boolean {
    val extractedGamesJSON = extractGamesFromPlayer(player, month, fileType = FileType.JSON)
    val extractedGamesPGN = extractGamesFromPlayer(player, month, fileType = FileType.PGN)
    return extractedGamesJSON == extractedGamesPGN
}


/**
 * Plays extracted games, catching [IllegalMoveException]s.
 * 
 * If an [IllegalMoveException] is caught, prints to the console that that move in the game was illegal.
 * 
 * In the end of playing all games, prints to the console the percentage of successfully played games.
 * @param extractedGames extracted games
 */
fun playExtractedGames(extractedGames: List<String>) {
    var successfullyPlayedGames = 0

    extractedGames.forEachIndexed { gameNum, game ->
        try {
            gameFromMoves(game.split(" "))
            successfullyPlayedGames++
        } catch (err: IllegalMoveException) {
            println("Move \"${err.move}\" in game ${gameNum + 1} considered illegal: ${err.message}")
        }
    }

    println("$successfullyPlayedGames successfully played games (${successfullyPlayedGames.toDouble() / extractedGames.size * 100}%).")
}


fun main() {
    println("\n---------------Extracting games---------------\n")
    val monthExtractions = listOf(
        MonthExtraction(player = "erik", month = "200910", fileType = FileType.JSON),
        MonthExtraction(player = "erik", month = "200911", fileType = FileType.JSON),
        MonthExtraction(player = "erik", month = "200912", fileType = FileType.JSON),
        MonthExtraction(player = "erik", month = "201001", fileType = FileType.JSON)
    )

    println("\n----------------Playing games----------------\n")

    monthExtractions.forEach { monthExtraction ->
        writeToFile(monthExtraction)
        playExtractedGames(monthExtraction.games)
    }
}
