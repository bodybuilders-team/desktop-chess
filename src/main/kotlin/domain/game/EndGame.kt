package domain.game

import domain.board.Board
import domain.pieces.Army
import domain.pieces.PieceType
import java.util.*


const val NUMBER_OF_MOVES_TO_DRAW = 100

//TODO - Dead position


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
fun Game.ended() = state in listOf(GameState.CHECKMATE, GameState.STALEMATE, GameState.TIE)


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
 * <-------------------------------------- Draws -------------------------------------->
 * You can check more about draw conditions here: https://www.chess.com/terms/draw-chess
 */


/**
 * Checks if the king of the [army] is in stalemate.
 *
 * The king is in stalemate if the king isn't in check, but it's the army's turn and the army has no valid moves.
 * @param army army of the king to stalemate
 * @return true if the king of the [army] is in stalemate
 */
fun Game.isKingInStaleMate(army: Army) =
    !board.isKingInCheck(board.getKingPosition(army), army) &&
            armyToPlay == army && !hasAvailableMoves(army)


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


/**
 * Checks if a game is tied by threefold rule:
 *
 * The threefold repetition rule states that if a game reaches the same position three times, a draw can be claimed.
 * A position is repeated if all pieces of the same kind and color are on identical squares,
 * and all possible moves are the same.
 *
 * @return true if the game is tied by threefold rule
 */
fun Game.isTiedByThreefold(): Boolean {
    val boards = mutableListOf(Board())

    moves.forEach { move ->
        val newBoard = boards.last().makeMove(move)
        boards.add(newBoard)

        if (Collections.frequency(boards, newBoard) == 3) return true
    }

    return false
}


/**
 * Checks if a game is tied by dead position:
 *
 * A dead position happens when neither player can legally checkmate the opponent's king.
 * If the game reaches this situation and the move that generated the position is legal, the game ends in a tie.
 *
 * @return true if the game is tied by dead position
 */
fun Game.isTiedByDeadPosition(): Boolean {
    // TODO: 21/12/2021 To be implemented
    return false
}

