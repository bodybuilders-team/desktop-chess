import domain.Move
import storage.GameState


/**
 * [GameState] implementation for testing.
 */
class GameStateStub : GameState {
    private val moves: MutableMap<String, MutableList<Move>> = mutableMapOf()

    override fun getLastMove(game: String): Move {
        require(moves[game] != null)
        return moves[game]!!.last()
    }

    override fun getAllMoves(game: String): List<Move> {
        require(moves[game] != null)
        return moves[game]!!
    }

    override fun postMove(game: String, move: Move): Boolean {
        require(moves[game] != null)
        moves[game]!!.add(move)

        return true
    }

    override fun createGame(game: String): Boolean {
        moves[game] = mutableListOf()
        return true
    }

    override fun getGame(game: String): String? {
        return if (game !in moves) null else game
    }
}
