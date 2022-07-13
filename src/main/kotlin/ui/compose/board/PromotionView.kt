@file:Suppress("FunctionName")

package ui.compose.board

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import domain.Session
import domain.game.armyToPlay
import domain.pieces.PieceType
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
