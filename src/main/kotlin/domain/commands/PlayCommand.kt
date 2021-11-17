package domain.commands

import domain.IllegalMoveException
import domain.Session
import domain.SessionState
import domain.Move
import storage.GameState


/**
 * Executes a move command if it corresponds to the rules
 * @param db database where the moves are stored
 * @param chess current chess game
 * @throws IllegalArgumentException if game has not been initialized yet
 * @throws IllegalArgumentException if it is not the user's turn
 * @throws IllegalArgumentException if move to be made not specified
 */
class PlayCommand(private val db: GameState, private val chess: Session) : Command {

    override fun execute(parameter: String?): Result<Session> {
        require(chess.state != SessionState.LOGGING) { "Can't play without a game: try open or join commands." }
        require(chess.state != SessionState.WAITING_FOR_OPPONENT) { "Wait for your turn: try refresh command." }
        requireNotNull(parameter) { "Missing move." }

        val move = Move(parameter, chess.board)
        if (chess.board.getPiece(move.from)!!.army == chess.army.other())
            throw IllegalMoveException(parameter, "You cannot move an opponent's piece.")

        val newBoard = chess.board.makeMove(move)
        db.postMove(chess.name, move)

        return Result.success(
            chess.copy(
                state = SessionState.WAITING_FOR_OPPONENT,
                board = newBoard,
                moves = chess.moves + move
            )
        )
    }
}
