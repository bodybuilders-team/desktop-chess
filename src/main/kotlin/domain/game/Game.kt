package domain.game

import domain.board.*
import domain.move.*
import domain.pieces.Army


/**
 * A chess game.
 * @property board game board
 * @property moves previously played moves
 */
data class Game(val board: Board, val moves: List<Move>)


/**
 * Different states the game can be in.
 */
enum class GameState {
    NO_CHECK,
    CHECK,
    CHECKMATE,
    STALEMATE,
    FIFTY_MOVE_RULE,
    THREE_FOLD,
    DEAD_POSITION
}


/**
 * Makes a move in the game.
 * @param move move to make
 * @return new game with the move made
 */
fun Game.makeMove(move: Move) =
    Game(board = board.makeMove(move), moves = moves + move)


/**
 * Returns the army playing in the current turn.
 * @return the army playing in the current turn
 */
val Game.armyToPlay
    get() = if (moves.size % 2 == 0) Army.WHITE else Army.BLACK


/**
 * Gets the current state of the game.
 * @return state of the game
 */
val Game.state
    get() =
        when {
            board.isKingInCheckMate(Army.WHITE) || board.isKingInCheckMate(Army.BLACK)  -> GameState.CHECKMATE
            isKingInStaleMate(Army.WHITE) || isKingInStaleMate(Army.BLACK)              -> GameState.STALEMATE
            isTiedByFiftyMoveRule()                                                     -> GameState.FIFTY_MOVE_RULE
            isTiedByThreefold()                                                         -> GameState.THREE_FOLD
            isTiedByDeadPosition()                                                      -> GameState.DEAD_POSITION
            board.isKingInCheck(Army.WHITE) || board.isKingInCheck(Army.BLACK)          -> GameState.CHECK
            else                                                                        -> GameState.NO_CHECK
        }
