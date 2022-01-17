package ui.compose.right_planel

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import domain.game.Game
import ui.compose.FONT_FAMILY
import ui.compose.FONT_SIZE


/**
 * Composable used to display a column with the moves already made in a chess game.
 * @param game chess game
 */
@Composable
fun MovesView(game: Game) {
    LazyColumn(state = LazyListState(game.moves.size / 2)) {
        game.moves.forEachIndexed { idx, move ->
            if (idx % 2 == 0) {
                item {
                    Row {
                        val round = idx / 2
                        Text(
                            text = " ${"$round.".padStart(4)} ${"$move".padEnd(8)}",
                            fontFamily = FONT_FAMILY,
                            fontSize = FONT_SIZE
                        )

                        if (idx != game.moves.size - 1) {
                            Text(
                                text = " - ${game.moves[idx + 1]}".padEnd(8),
                                fontFamily = FONT_FAMILY,
                                fontSize = FONT_SIZE
                            )
                        }
                    }
                }
            }
        }
    }
}
