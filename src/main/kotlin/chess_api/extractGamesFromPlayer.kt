package chess_api

import domain.gameFromMoves
import domain.move.IllegalMoveException
import org.bson.json.JsonObject
import java.io.File
import java.io.PrintWriter

//TODO("Allow different game variants (initial board differs)
// Game variants Chess960")

//TODO("Allow obtaining partial games initial board through FEN notation setup")


// https://www.chess.com/news/view/published-data-api#pubapi-general

// List of titled-player usernames.
// URL pattern: https://api.chess.com/pub/titled/{title-abbrev}
// Example: https://api.chess.com/pub/titled/GM

// Array of monthly archives available for this player.
// URL pattern: https://api.chess.com/pub/player/{username}/games/archives
// Example: https://api.chess.com/pub/player/erik/games/archives

// Gets JSON file containing all games of a player in a given month.
// URL pattern: https://api.chess.com/pub/player/{username}/games/{YYYY}/{MM}/pgn
// Example: https://api.chess.com/pub/player/erik/games/2009/10
// Downloaded file name: ChessCom_username_YYYYMM.pgn


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
 * Extract games from a player in a given month, given the filetype (JSON or PGN).
 *
 * Exact same games will be extracted regardless of filetype.
 * @param player player to get games from
 * @param month month to get games from
 * @param location type of the file where the games are
 * @return extracted games
 */
fun extractGames(player: String, month: String, location: Location): List<String> {
    val fileName = "$CHESS_API_FOLDER_LOCATION/${location} files/ChessCom_${player}_$month.${location}"
    val file = File(fileName)
    
    if (location != Location.API)
        require(file.exists()) { "File $fileName does not exist. Check the player and month or download the file." }
    
    val loggerFile = File("$CHESS_EXTRACTED_FOLDER_LOCATION/log/${player}/${month}/${location}.txt")

    File("$CHESS_EXTRACTED_FOLDER_LOCATION/log/${player}").mkdir()
    File("$CHESS_EXTRACTED_FOLDER_LOCATION/log/${player}/${month}").mkdir()
    
    val logger = loggerFile.printWriter()

    val whatToExtractString = "games from $player in ${month.substring(4..5)}/${month.substring(0..3)}"

    println("Extracting $whatToExtractString...")

    val games = when (location) {
        Location.API -> extractGamesFromPGN(
            getMonthPGNList("$CHESS_COM_API_URL/pub/player/erik/games/${month.substring(0..3)}/${month.substring(4..5)}"),
            logger
        )
        Location.JSON -> extractGamesFromJSON(file.readLines(), logger)
        Location.PGN -> TODO("") //extractGamesFromJSON(file.readLines(), logger) //extractGamesFromPGN(file.readLines(), logger)
    }

    logger.close()

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
fun extractGamesFromJSON(lines: List<String>, logger: PrintWriter): List<String> {
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
 * - time clock (clock game)
 * - turn number (clock game)
 * - check character '+'
 * - checkmate character '#'
 * - turn number
 * - who won
 *
 * @param pgnList list of PGN objects
 * @param logger logger to log information to
 * @return extracted games
 */
fun extractGamesFromPGN(pgnList: List<PGN>, logger: PrintWriter): List<String> {
    val ignoredGamesFiltered = pgnList.filterIndexed { idx, pgn ->
        val movesValid = pgn.moves.substringBefore(".") == "1"

        if (pgn.moves.first().isDigit() && !movesValid) {
            logger.println("Ignored game ${idx + 1} because it doesn't start on move 1.")
        }
        if (pgn.variant != null)
            logger.println("Ignored game ${idx + 1} because of different game variant \"${pgn.variant}\".")

        pgn.variant == null && movesValid
    }

    logger.println("Extracted ${ignoredGamesFiltered.size} from ${pgnList.size}.")
    
    return ignoredGamesFiltered.map { pgn ->
        pgn.moves.replace(Regex("\\{\\[%clk 0:\\d\\d:\\d\\d(\\.\\d)?]} "), "")
            .replace(Regex("\\d+\\.\\.\\. "), "")
            .replace("+", "")
            .replace("#", "")
            .replace(Regex("\\d+\\. "), "")
            .replace(Regex(" (0-1|1-0|1/2-1/2)"), "")
    }
}


/**
 * From a json string with PGN format, gets a list of PGN objects.
 * @param jsonString json in string
 * @return list of PGN objects
 */
fun getPGNListFromJSON(jsonString: String): List<PGN> {
    val json = JsonObject(jsonString)

    val games = json.toBsonDocument()["games"]!!.asArray()

    return games.map { PGN(it.asDocument()["pgn"]!!.asString().value.split("\n")) }
}


/**
 * Data class containing player, month, and games of the player in that month.
 * @property player player to get games from
 * @property month month to get games from
 * @property location locations to get the games from
 * @property games the extracted games
 */
data class MonthExtraction(val player: String, val month: String, val location: Location) {
    val games: List<String> = extractGames(player, month, location)

    override fun toString() = "${player}/${month}/${location}"
}


/**
 * Writes the games of a month, one in each line.
 * @param monthExtraction month extraction containing player, month, and games of the player in that month
 */
fun writeToFile(monthExtraction: MonthExtraction) {
    File("$CHESS_EXTRACTED_FOLDER_LOCATION/games/${monthExtraction.player}").mkdir()
    File("$CHESS_EXTRACTED_FOLDER_LOCATION/games/${monthExtraction.player}/${monthExtraction.month}").mkdir()
    
    val printWriter = File("$CHESS_EXTRACTED_FOLDER_LOCATION/games/${monthExtraction}.txt").printWriter()
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
    TODO("Read files from a month folder (extracted/games/player/month) and compare them.")
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
    
    val location = Location.API
    val player = "erik"
    
    val months = getMonthlyArchives(player).map { it.substring((it.length - 7) until it.length).replace("/", "") }
        .filterIndexed { index, _ ->  index < 20 }
    
    val monthExtractions = months.map { MonthExtraction(player = player, month = it, location = location) }

    println("\n----------------Playing games----------------\n")

    monthExtractions.forEach { monthExtraction ->
        writeToFile(monthExtraction)
        print("${monthExtraction.player}${monthExtraction.month} - ")
        playExtractedGames(monthExtraction.games)
    }
}
