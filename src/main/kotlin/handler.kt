import domain.commands.*
import domain.Session
import storage.GameState
import ui.console.*


/**
 * Association between command and corresponding display.
 * @property action command
 * @property display view of the command
 */
data class CommandHandler(val action: Command, val display: View)


/**
 * Gets the container bearing the associations between user entered strings and the corresponding CommandHandler.
 * @param chess current chess game
 * @param db database where the game is stored
 * @return the container with the command handler mappings
 */
fun buildCommandsHandler(chess: Session, db: GameState): Map<String, CommandHandler> {
    return mapOf(
        "open"    to CommandHandler(action = OpenCommand(db),            display = ::openView),
        "join"    to CommandHandler(action = JoinCommand(db),            display = ::joinView),
        "play"    to CommandHandler(action = PlayCommand(db, chess),     display = ::playView),
        "refresh" to CommandHandler(action = RefreshCommand(db, chess),  display = ::refreshView),
        "moves"   to CommandHandler(action = MovesCommand(db, chess),    display = ::movesView),
        "exit"    to CommandHandler(action = ExitCommand(),              display = { }),
        "help"    to CommandHandler(action = HelpCommand(chess),         display = ::helpView)
    )
}
