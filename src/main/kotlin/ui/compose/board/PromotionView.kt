package ui.compose.board

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import domain.Session
import domain.game.armyToPlay
import domain.move.Move
import domain.pieces.PieceType
import storage.GameStorage
import ui.compose.WINDOW_SCALE


// Constants
private val PROMOTION_BUTTON_PADDING = 10.dp * WINDOW_SCALE
private val PROMOTION_BUTTON_SIZE = 160.dp * WINDOW_SCALE
private val promotionPieces = listOf(PieceType.QUEEN, PieceType.BISHOP, PieceType.KNIGHT, PieceType.ROOK)


/**
 * Composable used to display a dialog with the promotion pieces.
 * @param session game session
 * @param onPieceTypeSelected function to be executed when the promotion piece type is selected
 */
@Composable
@OptIn(ExperimentalMaterialApi::class)
fun PromotionView(session: Session, onPieceTypeSelected: (Char) -> Unit) {
    var openDialog by mutableStateOf(true)

    if (openDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Promotion Piece") },
            text = { Text("Select Promotion Piece") },
            buttons = {
                Row(horizontalArrangement = Arrangement.Center) {
                    promotionPieces.forEach { piece ->
                        Button(
                            onClick = {
                                openDialog = false
                                onPieceTypeSelected(piece.symbol)
                            },
                            modifier = Modifier
                                .size(PROMOTION_BUTTON_SIZE)
                                .padding(PROMOTION_BUTTON_PADDING)
                        ) {
                            Image(
                                painter = painterResource(
                                    "${session.game.armyToPlay.toString().first().lowercase()}_${
                                        piece.toString().lowercase()
                                    }.png"
                                ),
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        )
    }
}


/**
 * Shows the promotion view and makes the move.
 *
 * @param session current session
 * @param move move to be made
 * @param availableMoves list of available moves
 * @param dataBase where the game is stored
 */
@Composable
fun ShowPromotionView(
    session: MutableState<Session>,
    move: MutableState<Move?>,
    availableMoves: MutableState<List<Move>>,
    dataBase: GameStorage
) {
    PromotionView(session.value) { pieceSymbol ->
        move.value = move.value!!.copy(promotion = pieceSymbol)
        makeMove(move, availableMoves, session, dataBase)
    }
}
