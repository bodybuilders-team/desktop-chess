import com.mongodb.client.MongoDatabase
import commands.Result

class MongoDbChess(private val database: MongoDatabase):commands.ChessCommands {
    override fun open(gameId: String): Result {
        TODO("Not yet implemented")
    }

    override fun join(gameId: String): Result {
        TODO("Not yet implemented")
    }

    override fun play(stringMove: String): Result {
        TODO("Not yet implemented")
    }

    override fun refresh(): Result {
        TODO("Not yet implemented")
    }

    override fun moves(): Result {
        TODO("Not yet implemented")
    }

    override fun exit(): Result {
        TODO("Not yet implemented")
    }

}
