package ui.compose

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import domain.Session
import domain.pieces.PieceType


// Constants
val PROMOTION_BUTTON_PADDING = 10.dp
val PROMOTION_BUTTON_SIZE = 160.dp
val promotionPieces = listOf(PieceType.QUEEN, PieceType.BISHOP, PieceType.KNIGHT, PieceType.ROOK)


/**
 * Composable used to display a dialog with the promotion pieces.
 * @param session game session
 * @param onPieceTypeSelected function to be executed when the promotion piece type is selected
 */
@Composable
@Preview
@OptIn(ExperimentalMaterialApi::class)
fun PromotionView(session: Session, onPieceTypeSelected: (Char) -> Unit) {
    var openDialog by mutableStateOf(true)

    if (openDialog) {
        AlertDialog(
            onDismissRequest = { openDialog = false },
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
                            modifier = Modifier.size(PROMOTION_BUTTON_SIZE).padding(PROMOTION_BUTTON_PADDING)
                        ) {
                            val painter = painterResource(
                                "${session.army.toString().first().lowercase()}_${piece.toString().lowercase()}.png"
                            )
                            Image(
                                painter = painter,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        )
    }
}
