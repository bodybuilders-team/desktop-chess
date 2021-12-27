package ui.compose

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import domain.pieces.PieceType


// Constants
val promotionPieces = listOf(PieceType.QUEEN, PieceType.BISHOP, PieceType.KNIGHT, PieceType.ROOK)


/**
 * Composable used to display a box with the moves already made in a chess game.
 */
@Composable
@Preview
@OptIn(ExperimentalMaterialApi::class)
fun PromotionView(onPieceTypeSelected: (Char) -> Unit) {
    var openDialog by mutableStateOf(true)

    if (openDialog) {
        AlertDialog(
            onDismissRequest = { openDialog = false },
            title = { Text("Promotion Piece") },
            text = { Text("Select Promotion Piece") },
            buttons = {
                Row (
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    promotionPieces.forEach { piece ->
                        Button(
                            onClick = {
                                openDialog = false
                                onPieceTypeSelected(piece.symbol)
                            }
                        ) {
                            val painter = painterResource(
                                "w_${piece.toString().lowercase()}.png" // TODO(Mudar a cor tendo em conta o army da session)
                            )
                            Image(
                                painter = painter,
                                contentDescription = null,
                                modifier = Modifier.size(80.dp)
                            )
                        }
                    }
                }
            }
        )
    }
}
