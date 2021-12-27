package ui.compose.menu

import WINDOW_PADDING
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import ui.compose.*


// Constants
val MENU_WIDTH = 260.dp
val MENU_HEIGHT = BOARD_HEIGHT
val MENU_TITLE_SIZE = 40.sp
val MENU_BUTTON_WIDTH = 200.dp
val MENU_BUTTON_HEIGHT = 60.dp
val MENU_BUTTON_MODIFIER = Modifier.size(MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT).padding(PROMOTION_BUTTON_PADDING)


/**
 * Composable used to display a column with the menu.
 * @param onOptionSelected function to be executed when the menu option is selected
 */
@Composable
fun MenuView(onOptionSelected: (String?) -> Unit) {
    Column(
        modifier = Modifier.padding(start = WINDOW_PADDING)
            .width(MENU_WIDTH)
            .height(MENU_HEIGHT)
            .background(Color(WHITE)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to Desktop Chess",
            fontSize = MENU_TITLE_SIZE,
            textAlign = TextAlign.Center
        )
        Button(
            onClick = { onOptionSelected("open") },
            modifier = MENU_BUTTON_MODIFIER
        ) {
            Text("Open a game")
        }
        Button(
            onClick = { onOptionSelected("join") },
            modifier = MENU_BUTTON_MODIFIER
        ) {
            Text("Join a game")
        }
        Button(
            onClick = { onOptionSelected("exit") },
            modifier = MENU_BUTTON_MODIFIER
        ) {
            Text("Exit")
        }
    }
}
