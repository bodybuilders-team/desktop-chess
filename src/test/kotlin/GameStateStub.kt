import domain.Move
import storage.GameState


/**
 * [GameState] implementation for testing.
 */
class GameStateStub : GameState {
    private val moves: MutableMap<String, MutableList<Move>> = mutableMapOf()

    override fun getLastMove(game: String): Move {
        val moves = moves[game]
        requireNotNull(moves)
        return moves.last()
    }

    override fun getAllMoves(game: String): List<Move> {
        val moves = moves[game]
        requireNotNull(moves)
        return moves
    }

    override fun postMove(game: String, move: Move): Boolean {
        val moves = moves[game]
        requireNotNull(moves)
        moves.add(move)

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
