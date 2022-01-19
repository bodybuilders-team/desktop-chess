package ui.compose.board

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import domain.Session
import domain.commands.PlayCommand
import domain.move.Move
import storage.GameStorage


/**
 * Composable that makes a move, showing the promotion view if the move is a promotion.
 * @param move move to make
 * @param availableMoves all possible moves
 * @param session game session
 * @param dataBase database where the games are stored
 */
@Composable
fun MakeMoveComposable(
    move: MutableState<Move?>,
    availableMoves: MutableState<List<Move>>,
    session: MutableState<Session>,
    dataBase: GameStorage
) {
    when {
        move.value?.promotion != null ->
            PromotionView(session.value) { pieceSymbol ->
                move.value = move.value!!.copy(promotion = pieceSymbol)
                makeMove(move, availableMoves, session, dataBase)
            }
        else -> makeMove(move, availableMoves, session, dataBase)
    }
}


/**
 * Makes a move in the session game, clearing the available moves.
 * @param move move to make
 * @param dataBase database where the games are stored
 * @param session game session
 * @param availableMoves all possible moves of the selected position
 */
private fun makeMove(
    move: MutableState<Move?>,
    availableMoves: MutableState<List<Move>>,
    session: MutableState<Session>,
    dataBase: GameStorage
) {
    session.value = PlayCommand(dataBase, session.value).execute(move.value.toString()).getOrThrow()
    availableMoves.value = emptyList()
    move.value = null
}
