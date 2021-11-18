package domain.commands

import domain.Session


/**
 * Terminates the application
 */
class ExitCommand : Command {

    override fun execute(parameter: String?): Result<Session> {
        return Result.failure(Throwable("Exiting Game."))
    }
}
