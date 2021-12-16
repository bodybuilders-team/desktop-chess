package chess_api

import org.bson.BsonArray
import org.bson.BsonDocument
import org.bson.BsonValue
import org.bson.json.JsonObject
import java.lang.Exception

/*
 * Prettier Bson (PBson) by Nyckollas Brandão
 * Types only verified in runtime but the syntax is much simpler.
 */


operator fun JsonObject.get(key: String): BsonValue {
    require(contains(key)) { "Key \"$key\" is not valid for this object." }
    return toBsonDocument()[key]!!
}

operator fun JsonObject.contains(key: String) = toBsonDocument().containsKey(key)

operator fun BsonValue.get(index: Int): BsonValue = asArray()[index]!!

operator fun BsonValue.get(key: String): BsonValue = asDocument()[key]!!

operator fun BsonValue.contains(element: String): Boolean {
    when(this){
        is BsonDocument -> if(asDocument().containsKey(element)) return true
        is BsonArray -> if (valueAsListOfStrings.contains(element)) return true
        else -> throw Exception("This value doesn't allow \"contains\" method.")
    }
    
    return false
}

val BsonValue.valueAsString: String
    get() = asString().value

val BsonValue.valueAsInt: Int
    get() = asInt32().value

val BsonValue.valueAsListOfStrings: List<String>
    get() = asArray().values.map { it.valueAsString }

val BsonValue.valueAsListOfInts: List<Int>
    get() = asArray().values.map { it.valueAsInt }

val BsonValue.valueAsListOfObjects: List<BsonDocument>
    get () = asArray().values.map { it.asDocument() }

fun BsonValue.filter(predicate: (BsonValue) -> Boolean) = asArray().values.filter(predicate)
