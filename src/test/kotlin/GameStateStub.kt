import domain.move.Move
import storage.GameState


/**
 * [GameState] implementation for testing.
 */
class GameStateStub : GameState {
    private val database: MutableMap<String, MutableList<Move>> = mutableMapOf()

    override fun getAllMoves(gameName: String): List<Move> {
        val moves = database[gameName]
        requireNotNull(moves)
        return moves
    }

    override fun postMove(gameName: String, move: Move): Boolean {
        val moves = database[gameName]
        requireNotNull(moves)
        moves.add(move)

        return true
    }

    override fun createGame(gameName: String) {
        require(!gameExists(gameName))
        database[gameName] = mutableListOf()
    }

    override fun gameExists(gameName: String): Boolean {
        return gameName in database
    }
}
