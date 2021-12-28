package ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.*
import domain.game.Game
import ui.compose.app.WINDOW_PADDING
import ui.compose.board.BOARD_HEIGHT
import ui.compose.board.WHITE


// Constants
val MOVES_WIDTH = 320.dp
val MOVES_HEIGHT = BOARD_HEIGHT
val FONT_FAMILY = FontFamily.Monospace
val FONT_SIZE = 20.sp


/**
 * Composable used to display a column with the moves already made in a chess game.
 * @param game chess game
 */
@Composable
fun MovesView(game: Game) {
    LazyColumn(
        state = LazyListState(game.moves.size / 2),
        modifier = Modifier
            .padding(start = WINDOW_PADDING)
            .width(MOVES_WIDTH)
            .height(MOVES_HEIGHT)
            .background(Color(WHITE))
    ) {
        game.moves.forEachIndexed { idx, move ->
            if (idx % 2 == 0) {
                item {
                    Row {
                        val round = idx / 2
                        Text(
                            text = " ${"$round.".padEnd(4)} ${"$move".padEnd(8)} - ",
                            fontFamily = FONT_FAMILY,
                            fontSize = FONT_SIZE
                        )

                        if (idx != game.moves.size - 1)
                            Text(
                                text = "${game.moves[idx + 1]}".padEnd(8),
                                fontFamily = FONT_FAMILY,
                                fontSize = FONT_SIZE
                            )
                    }
                }
            }
        }
    }
}
