package ui.console

import domain.*
import domain.game.armyToPlay


/**
 * Returns the prompt indicating the game´s turn to the user
 * @param session current session
 * @return prompt
 */
fun getPrompt(session: Session): String {
    if (session.name.isEmpty()) return ""
    val turn = session.game.armyToPlay.toString()
    return "${session.name}:${turn.first() + turn.substring(1).lowercase()}"
}


/**
 * Reads the command entered by the user
 * @return Pair of command and its arguments
 */
fun readCommand(questString: String): Pair<String, String?> {
    print("$questString> ")
    val input = readLn().trim()
    val command = input.substringBefore(' ').lowercase()
    val argument = if (' ' in input) input.substringAfterLast(' ') else null
    return Pair(command, argument)
}


/**
 * Let's use this while we don't get to Kotlin v1.6
 */
private fun readLn() = readLine()!!
