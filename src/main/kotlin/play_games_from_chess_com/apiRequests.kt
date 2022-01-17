package play_games_from_chess_com

import org.bson.json.JsonObject
import java.net.URI
import java.net.http.*


// https://www.chess.com/news/view/published-data-api#pubapi-general

// List of titled-player usernames.
// URL pattern: https://api.chess.com/pub/titled/{title-abbrev}
// Example: https://api.chess.com/pub/titled/GM

// Array of monthly archives available for this player.
// URL pattern: https://api.chess.com/pub/player/{username}/games/archives
// Example: https://api.chess.com/pub/player/erik/games/archives

// Gets JSON file containing all games of a player in a given month.
// URL pattern: https://api.chess.com/pub/player/{username}/games/{YYYY}/{MM}
// Example: https://api.chess.com/pub/player/erik/games/2009/10


const val CHESS_COM_API_URL = "https://api.chess.com"


/**
 * Gets a list of all player usernames with the title [titleAbbrev]
 * @param titleAbbrev title of the player
 */
fun getPlayersFromTitle(titleAbbrev: String): List<String> {
    val json = getJsonResponse("$CHESS_COM_API_URL/pub/titled/$titleAbbrev")

    if (json.json == "{\"code\":0,\"message\":\"Data provider not found for key \\\"/pub/titled/$titleAbbrev\\\".\"}")
        throw Exception("Title abbreviation \"$titleAbbrev\" does not exist.")

    return json.toBsonDocument()["players"]!!.asArray().map { it.asString().value }
}


/**
 * Returns from a [player] a list of month uri's containing game information on games the player played that month.
 * @param player to get list of months from
 */
fun getMonthlyArchives(player: String): List<String> {
    val json = getJsonResponse("$CHESS_COM_API_URL/pub/player/$player/games/archives")

    if (json.json == "{\"code\":0,\"message\":\"User \\\"$player\\\" not found.\"}")
        throw Exception("Player \"$player\" does not exist.")

    return json.toBsonDocument()["archives"]!!.asArray().map { it.asString().value }
}


/**
 * Gets pgn file as list of strings containing information on all the games played in a month by a specific player.
 * @param player player to get games from
 * @param month month when the games where played
 */
fun getMonthPGNList(player: String, month: Month): List<PGN> {
    val uri =
        "$CHESS_COM_API_URL/pub/player/$player/games/${month.toString(monthFirst = false, separatingString = "/")}"

    val json = getJsonResponse(uri)

    if (json.json == "{\"code\":0,\"message\":\"Data provider not found for key \\\"${
            uri.substringAfter(CHESS_COM_API_URL)
        }\\\".\"}"
    ) {
        throw Exception("Unknown month for player.")
    }

    return getPGNListFromJSON(json)
}


/**
 * Gets a json response (as JsonObject) from a get request to the [uri].
 * @param uri to make get request to
 * @return json response
 */
fun getJsonResponse(uri: String): JsonObject {
    val client: HttpClient? = HttpClient.newHttpClient()

    val request = HttpRequest.newBuilder(
        URI.create(uri)
    )
        .header("accept", "application/json")
        .build()

    val response = client?.sendAsync(request, HttpResponse.BodyHandlers.ofString())!!
        .get()

    return JsonObject(response.body())
}
