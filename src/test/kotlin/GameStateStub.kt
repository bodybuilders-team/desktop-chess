import domain.Move
import storage.GameState


/**
 * [GameState] implementation for testing.
 */
class GameStateStub : GameState {
    private val database: MutableMap<String, MutableList<Move>> = mutableMapOf()

    override fun getAllMoves(game: String): List<Move> {
        val moves = database[game]
        requireNotNull(moves)
        return moves
    }

    override fun postMove(game: String, move: Move): Boolean {
        val moves = database[game]
        requireNotNull(moves)
        moves.add(move)

        return true
    }

    override fun createGame(game: String) {
        require(!gameExists(game))
        database[game] = mutableListOf()
    }

    override fun gameExists(game: String): Boolean {
        return game in database
    }
}
