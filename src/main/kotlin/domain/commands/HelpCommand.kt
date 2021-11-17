package domain.commands

import domain.Session


/**
 * Prints all the commands of the application.
 * @param chess current chess game
 */
class HelpCommand(private val chess: Session) : Command {

    override fun execute(parameter: String?): Result<Session> {
        return Result.success(chess)
    }
}
