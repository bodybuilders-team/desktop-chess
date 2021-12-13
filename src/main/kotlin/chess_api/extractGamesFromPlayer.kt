package chess_api

import domain.gameFromMoves
import domain.move.IllegalMoveException
import java.io.File

//TODO("Allow different game variants (initial board differs)
// Game variants Chess960")

//TODO("Allow obtaining initial board through FEN notation setup")

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

enum class FILE_TYPE {
    PGN,
    JSON
}

fun extractGamesFromPlayer(player: String, month: String, fileType: FILE_TYPE): List<String> {
    val type = fileType.name.lowercase()
    val fileName =
        "src/main/kotlin/chess_api/${type} files/ChessCom_${player}_$month.${type}"
    val file = File(fileName)
    require(file.exists()) { "File $fileName does not exist. Check the player and month or download the file." }

    val games = when (fileType) {
        FILE_TYPE.PGN -> extractGamesFromPGN(file.readLines())
        FILE_TYPE.JSON -> extractGamesFromJSON(file.readLines())
    }

    val printWriter =
        File("src/main/kotlin/chess_api/extracted games/ChessCom_${player}_${month}_${type}.txt").printWriter()
    games.forEach { game ->
        printWriter.println(game)
    }
    printWriter.close()

    println("${games.size} games extracted.")

    return games
}


fun extractGamesFromJSON(lines: List<String>): List<String> {
    val pgn = lines.filter {
        it.contains("pgn")
    }.map {
        it.substringAfter("\"pgn\": \"")
            .dropLast(2)
            .split("\\n")
    }

    return extractGamesFromPGN(pgn.flatten())
}


fun extractGamesFromPGN(lines: List<String>): List<String> {
    var gameCount = 0
    var gameVariant = ""
    var ignoreNext = false

    return lines.filter { line ->
        if (line.startsWith("[Variant")) {
            gameVariant = line.substringAfter("\"").substringBefore("\"").replace("\\", "")
            ignoreNext = true
            false
        } else if (line.isNotBlank() && line.first() == '1') {
            gameCount++
            if (ignoreNext) println("Ignored game $gameCount because of different game variant \"$gameVariant\"")
            !ignoreNext.also { ignoreNext = false }
        } else false
    }.map {
        it.replace(Regex("\\{\\[%clk 0:0\\d:\\d\\d(\\.\\d)?]} "), "") // time clock (clock game)
            .replace(Regex("\\d+\\.\\.\\. "), "")   // turn number (clock game)
            .replace("+", "").replace("#", "")      // check character '+' and checkmate character '#'
            .replace(Regex("\\d+\\. "), "")         // turn number
            .replace(Regex(" (0-1|1-0)"), "")        // who won
    }.sorted()
}


fun playExtractedGames(extractedGames: List<String>) {
    var successfullyPlayedGames = 0

    extractedGames.forEachIndexed { gameNum, game ->
        try {
            gameFromMoves(game.split(" "))
            successfullyPlayedGames++
        } catch (err: IllegalMoveException) {
            println("Move ${err.move} in game ${gameNum + 1} considered illegal: ${err.message}")
        }
    }

    println("$successfullyPlayedGames successfully played games (${successfullyPlayedGames.toDouble() / extractedGames.size * 100}%).")
}

fun main() {
    playExtractedGames(extractGamesFromPlayer(player = "erik", month = "200910", fileType = FILE_TYPE.JSON))
    playExtractedGames(extractGamesFromPlayer(player = "erik", month = "200911", fileType = FILE_TYPE.JSON))
    playExtractedGames(extractGamesFromPlayer(player = "erik", month = "200912", fileType = FILE_TYPE.JSON))
    playExtractedGames(extractGamesFromPlayer(player = "erik", month = "201001", fileType = FILE_TYPE.JSON))

    /*
    val extractedGamesJSON = extractGamesFromPlayer(player = "erik", month = "200910", fileType = FILE_TYPE.JSON)
    val extractedGamesPGN = extractGamesFromPlayer(player = "erik", month = "200910", fileType = FILE_TYPE.PGN)
    require(extractedGamesJSON == extractedGamesPGN) { "Extracted games should be the same regardless of file type." }
    */
}