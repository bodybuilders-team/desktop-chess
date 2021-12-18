package domain.game

import domain.board.Board
import domain.pieces.Army
import domain.pieces.PieceType


const val NUMBER_OF_MOVES_TO_DRAW = 100

//TODO - Dead position and Three-fold repetition


/**
 * Checks if the game ended.
 *
 * A game ends when:
 * - there's a checkmate
 * - there's a stalemate
 * - the 50 move rule is applied
 *
 * @return true if the game ended
 */
fun Game.ended() = getState() in listOf(GameState.CHECKMATE, GameState.STALEMATE, GameState.TIE)


/**
 * Checks if the king of the [army] is in check.
 * @param army army of the king to check
 * @return true if the king is in check
 */
fun Board.isKingInCheck(army: Army) = isKingInCheck(getKingPosition(army), army)


/**
 * Checks if the king of the [army] is in check mate.
 *
 * The king is in checkmate if all these conditions apply:
 * - The king is in check;
 * - The king is not protectable, meaning no ally piece can remove the check, protecting the king (by being placed in front or killing the attacker);
 * - The king has nowhere to go. All adjacent positions are either occupied or attacked by an enemy piece.
 * @param army army of the king to check mate
 * @return true if the king is in check mate
 */
fun Board.isKingInCheckMate(army: Army): Boolean {
    val kingPos = getKingPosition(army)
    return isKingInCheck(kingPos, army) && !isKingProtectable(kingPos, army) && !canKingMove(kingPos, army)
}


/**
 * Checks if the king of the [army] is in stalemate.
 *
 * The king is in stalemate if the king isn't in check, but it's the army's turn and the army has no valid moves.
 * @param army army of the king to stalemate
 * @return true if the king of the [army] is in stalemate
 */
fun Game.isKingInStaleMate(army: Army) =
    !board.isKingInCheck(board.getKingPosition(army), army) &&
            currentTurnArmy == army && !hasAvailableMoves(army)


/**
 * Checks if the game is tied by the 50 move rule:
 *
 * The fifty-move rule in chess states that a player can claim a draw if no capture has been made and no pawn
 * has been moved in the last fifty consecutive moves.
 *
 * It is designed to stop endgames going on forever when one (or both) players have no idea how to end the game.
 *
 * @return true if the game is tied by the 50 move rule
 */
fun Game.isTiedByFiftyMoveRule() =
    moves.size >= NUMBER_OF_MOVES_TO_DRAW && moves.takeLast(NUMBER_OF_MOVES_TO_DRAW).none { move ->
        move.capture || move.symbol == PieceType.PAWN.symbol
    }
