import domain.*
import ui.console.*


/**
 * Associations between command and corresponding display.
 * @property action command
 * @property display view of the command
 */
data class CommandHandler(val action: Command, val display: View)


/**
 * Gets the container bearing the associations between user entered strings and the corresponding CommandHandler.
 * @return the container with the command handler mappings
 */
fun buildCommandsHandler(): Map<String, CommandHandler> {
    return mapOf(
        "open"    to CommandHandler(::open, ::openView),
        "join"    to CommandHandler(::join, ::joinView),
        "play"    to CommandHandler(::play, ::playView),
        "refresh" to CommandHandler(::refresh, ::refreshView),
        "moves"   to CommandHandler(::moves, ::movesView),
        "exit"    to CommandHandler(::exit) { },
        "help"    to CommandHandler(::help, ::helpView)
    )
}
