package domain.commands

import domain.Session


/**
 * Prints all the commands of the application.
 * @param session current session
 */
class HelpCommand(private val session: Session) : Command {

    override fun execute(parameter: String?): Result<Session> {
        return Result.success(session)
    }
}
