package ui.compose.board

import androidx.compose.runtime.MutableState
import domain.Session
import domain.commands.PlayCommand
import domain.move.Move
import storage.GameStorage

/**
 * Makes a move in the session game, clearing the available moves.
 *
 * @param move move to make
 * @param availableMoves all possible moves of the selected position
 * @param session game session
 * @param gameStorage where the games are stored
 */
suspend fun makeMove(
    move: MutableState<Move?>,
    availableMoves: MutableState<List<Move>>,
    session: MutableState<Session>,
    gameStorage: GameStorage
) {
    session.value = PlayCommand(gameStorage, session.value).execute(move.value.toString()).getOrThrow()
    availableMoves.value = emptyList()
    move.value = null
}
