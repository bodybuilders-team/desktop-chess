import domain.Session
import domain.commands.Command
import domain.commands.ExitCommand
import domain.commands.HelpCommand
import domain.commands.JoinCommand
import domain.commands.MovesCommand
import domain.commands.OpenCommand
import domain.commands.PlayCommand
import domain.commands.RefreshCommand
import storage.GameStorage
import ui.console.View
import ui.console.helpView
import ui.console.joinView
import ui.console.movesView
import ui.console.openView
import ui.console.playView
import ui.console.refreshView

/**
 * Association between command and corresponding display.
 * @property action command
 * @property display view of the command
 */
data class CommandHandler(
    val action: Command,
    val display: View
)

/**
 * Gets the container bearing the associations between user entered strings and the corresponding CommandHandler.
 * @param session current session
 * @param db database where the game is stored
 * @return the container with the command handler mappings
 */
fun buildCommandsHandler(session: Session, db: GameStorage): Map<String, CommandHandler> {
    return mapOf(
        "open" to CommandHandler(action = OpenCommand(db), display = ::openView),
        "join" to CommandHandler(action = JoinCommand(db), display = ::joinView),
        "play" to CommandHandler(action = PlayCommand(db, session), display = ::playView),
        "refresh" to CommandHandler(action = RefreshCommand(db, session), display = ::refreshView),
        "moves" to CommandHandler(action = MovesCommand(db, session), display = ::movesView),
        "exit" to CommandHandler(action = ExitCommand(), display = { }),
        "help" to CommandHandler(action = HelpCommand(session), display = ::helpView)
    )
}
