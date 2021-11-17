import domain.commands.*
import storage.GameState
import ui.console.*


/**
 * Associations between command and corresponding display.
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
        "open"    to CommandHandler(OpenCommand(db), ::openView),
        "join"    to CommandHandler(JoinCommand(db), ::joinView),
        "play"    to CommandHandler(PlayCommand(db, chess), ::playView),
        "refresh" to CommandHandler(RefreshCommand(db, chess), ::refreshView),
        "moves"   to CommandHandler(MovesCommand(db, chess), ::movesView),
        "exit"    to CommandHandler(ExitCommand()) { },
        "help"    to CommandHandler(HelpCommand(chess), ::helpView)
    )
}
