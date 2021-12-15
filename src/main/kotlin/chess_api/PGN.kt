package chess_api

import org.bson.json.JsonObject

/**
 * Represents a chess game in PGN format (Portable Game Notation).
 * 
 * https://en.wikipedia.org/wiki/Portable_Game_Notation
 */
data class PGN(val pgnInList: List<String>){
    lateinit var event: String
    lateinit var site: String
    lateinit var date: String
    lateinit var round: String
    lateinit var white: String
    lateinit var black: String
    lateinit var result: String
    
    var variant: String? = null
    var setUp: String? = null
    var fen: String? = null
    
    var tournament: String? = null
    var currentPosition: String? = null
    var timezone: String? = null
    var eco: String? = null
    var ecoUrl: String? = null
    var utcDate: String? = null
    var utcTime: String? = null
    var whiteElo: String? = null
    var blackElo: String? = null
    var timeControl: String? = null
    var termination: String? = null
    var startTime: String? = null
    var endDate: String? = null
    var endTime: String? = null
    var link: String? = null
    var moves: String = ""
    
    val hashmap = HashMap<String, String>() //TODO("Use hashmap instead of all these properties.")
    operator fun get(string: String): String {
        return ""
    }
    
    init {
        fun String.substringBetweenQuotationMarks() = substringAfter("\"").substringBeforeLast("\"")
        
        pgnInList.filter { it.isNotBlank() }.forEach { 
            val value = it.substringBetweenQuotationMarks()
            
            if(it.first() == '['){
                when(it.substringAfter("[").substringBefore(" ")){
                    "Event" -> event = value
                    "Site" -> site = value
                    "Date" -> date = value
                    "Round" -> round = value
                    "White" -> white = value
                    "Black" -> black = value
                    "Result" -> result = value
                    
                    "Variant" -> variant = value
                    "SetUp" -> setUp = value
                    "FEN" -> fen = value
                    
                    "Tournament" -> tournament = value
                    "CurrentPosition" -> currentPosition = value
                    "Timezone" -> timezone = value
                    "ECO" -> eco = value
                    "ECOUrl" -> ecoUrl = value
                    "UTCDate" -> utcDate = value
                    "UTCTime" -> utcTime = value
                    "WhiteElo" -> whiteElo = value
                    "BlackElo" -> blackElo = value
                    "TimeControl" -> timeControl = value
                    "Termination" -> termination = value
                    "StartTime" -> startTime = value
                    "EndDate" -> endDate = value
                    "EndTime" -> endTime = value
                    "Link" -> link = value
                }
            }
            else {
                moves = it
            }
        }
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

    return games.filter { "pgn" in it.asDocument() }.map { PGN(it.asDocument()["pgn"]!!.asString().value.split("\n")) }
}


fun main(){
    
    val pgn = PGN(listOf(
        "[Event \"Hello!\"]",
        "[Site \"Chess.com\"]",
        "[Date \"2009.09.17\"]",
        "[Round \"-\"]",
        "[White \"jsssuperstar\"]",
        "[Black \"erik\"]",
        "[Result \"0-1\"]",
        "[CurrentPosition \"8/8/8/p7/8/6k1/4n1b1/6K1 w - - 51 90\"]",
        "[Timezone \"UTC\"]",
        "[ECO \"A04\"]",
        "[ECOUrl \"https://www.chess.com/openings/Reti-Opening-Pirc-Invitation\"]",
        "[UTCDate \"2009.09.17\"]",
        "[UTCTime \"21:40:19\"]",
        "[WhiteElo \"1306\"]",
        "[BlackElo \"2061\"]",
        "[TimeControl \"1/259200\"]",
        "[Termination \"erik won by checkmate\"]",
        "[StartTime \"21:40:19\"]",
        "[EndDate \"2009.10.31\"]",
        "[EndTime \"17:53:17\"]",
        "[Link \"https://www.chess.com/game/daily/28103371\"]",
        "1. Nf3 d6 2. Nc3 g6"
    ))
    
    println(pgn.event)
    println(pgn.site)
    println(pgn.date)
    println(pgn.round)
    println(pgn.white)
    println(pgn.black)
    println(pgn.result)
    println(pgn.moves)
}
