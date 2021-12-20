package chess_api

import org.bson.json.JsonObject

/**
 * Represents a chess game in PGN format (Portable Game Notation).
 *
 * https://en.wikipedia.org/wiki/Portable_Game_Notation
 */
data class PGN(val pgnInList: List<String>) {
    private val hashmap = HashMap<String, String>()

    operator fun get(string: String): String? {
        return hashmap[string];
    }

    val moves: String
        get() = hashmap["Moves"]!!

    init {
        fun String.substringBetweenQuotationMarks() = substringAfter("\"").substringBeforeLast("\"")

        pgnInList.filter { it.isNotBlank() }.forEach {
            val value = it.substringBetweenQuotationMarks()

            if (it.first() == '[') {
                hashmap[it.substringAfter("[").substringBefore(" ")] = value
            } else {
                hashmap["Moves"] = it
            }
        }
    }
}


/**
 * From a json string, gets a list of PGN objects.
 * @param jsonString json in string
 * @return list of PGN objects
 */
fun getPGNListFromJSON(jsonString: String) = getPGNListFromJSON(JsonObject(jsonString))


/**
 * From a json object, gets a list of PGN objects.
 * @param json json object
 * @return list of PGN objects
 */
fun getPGNListFromJSON(json: JsonObject): List<PGN> =
    json.toBsonDocument()["games"]!!.asArray().filter { "pgn" in it.asDocument() }.map {
        PGN(it.asDocument()["pgn"]!!.asString().value.split("\n"))
    }
