package domain.commands

import Session


/**
 * Terminates the application by saving the actual state of the game
 */
class ExitCommand : Command {

    override fun execute(parameter: String?): Result<Session> {
        return Result.failure(Throwable("Exiting Game."))
    }
}
