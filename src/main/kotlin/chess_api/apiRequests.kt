package chess_api

import org.bson.json.JsonObject
import java.net.URI
import java.net.http.*


const val CHESS_COM_API_URL = "https://api.chess.com"


/**
 * Gets a list of all player usernames with the title [titleAbbrev]
 * @param titleAbbrev title of the player
 */
fun getPlayersFromTitle(titleAbbrev: String): List<String> {
    val json = getJsonResponse("$CHESS_COM_API_URL/pub/titled/$titleAbbrev")

    if (json.json == "{\"code\":0,\"message\":\"Data provider not found for key \\\"/pub/titled/$titleAbbrev\\\".\"}")
        throw Exception("Title abbreviation \"$titleAbbrev\" does not exist.")

    return json.toBsonDocument()["players"]!!
        .asArray().map {
            it.asString().value
        }
}


/**
 * Returns from a [player] a list of month uri's containing game information on games the player played that month.
 * @param player to get list of months from
 */
fun getMonthlyArchives(player: String): List<String> {
    val json = getJsonResponse("$CHESS_COM_API_URL/pub/player/$player/games/archives")

    if (json.json == "{\"code\":0,\"message\":\"User \\\"$player\\\" not found.\"}")
        throw Exception("Player \"$player\" does not exist.")

    return json.toBsonDocument()["archives"]!!
        .asArray().map {
            it.asString().value
        }
}


/**
 * Gets pgn file as list of strings containing information on all the games played in a month by a specific player.
 * @param uri uri of the monthly archive
 */
fun getMonthPGNList(uri: String): List<PGN> {
    require(Regex("^$CHESS_COM_API_URL/pub/player/(.+)/games/\\d{4}/\\d{2}\$").containsMatchIn(uri)) {
        "Wrong format for monthly games. Use: https://api.chess.com/pub/player/{username}/games/{YYYY}/{MM}"
    }

    val json = getJsonResponse(uri)

    if (json.json == "{\"code\":0,\"message\":\"Data provider not found for key \\\"${
            uri.substringAfter(
                CHESS_COM_API_URL
            )
        }\\\".\"}"
    ) {
        throw Exception("Unknown month for player.")
    }

    return getPGNListFromJSON(json.json)
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

    val response = client?.send(request, HttpResponse.BodyHandlers.ofString())!!

    return JsonObject(response.body())
}
