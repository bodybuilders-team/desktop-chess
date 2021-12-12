import domain.Session
import domain.SessionState
import domain.board.Check
import domain.gameFromMoves
import domain.pieces.Army
import ui.console.*


object ViewsTests {

    data class ViewTest(val view: View, val name: String, val session: Session)

    private val defaultSession = Session(name = "test", SessionState.YOUR_TURN, Army.WHITE, gameFromMoves(), Check.NO_CHECK)
    
    fun openViewTests(){
        testViews(listOf(
            ViewTest(::openView, "Open game without check",
                defaultSession.copy(state = SessionState.YOUR_TURN, currentCheck = Check.NO_CHECK)),

            ViewTest(::openView, "Open game with check",
                defaultSession.copy(state = SessionState.YOUR_TURN, currentCheck = Check.CHECK)),

            ViewTest(::openView, "Open game with checkmate",
                defaultSession.copy(state = SessionState.ENDED, currentCheck = Check.CHECKMATE)),

            ViewTest(::openView, "Open game with stalemate",
                defaultSession.copy(state = SessionState.ENDED, currentCheck = Check.STALEMATE))
        ))
    }

    fun joinViewTests(){
        testViews(listOf(
            ViewTest(::joinView, "Join game without check",
                defaultSession.copy(state = SessionState.YOUR_TURN, currentCheck = Check.NO_CHECK)),

            ViewTest(::joinView, "Join game with check",
                defaultSession.copy(state = SessionState.YOUR_TURN, currentCheck = Check.CHECK)),

            ViewTest(::joinView, "Join game with checkmate",
                defaultSession.copy(state = SessionState.ENDED, currentCheck = Check.CHECKMATE)),

            ViewTest(::joinView, "Join game with stalemate",
                defaultSession.copy(state = SessionState.ENDED, currentCheck = Check.STALEMATE))
        ))
    }

    fun playViewTests(){
        testViews(listOf(
            ViewTest(::playView, "Play didn't originate check",
                defaultSession.copy(state = SessionState.WAITING_FOR_OPPONENT, currentCheck = Check.NO_CHECK)),

            ViewTest(::playView, "Play originated a check",
                defaultSession.copy(state = SessionState.WAITING_FOR_OPPONENT, currentCheck = Check.CHECK)),

            ViewTest(::playView, "Play originated a checkmate",
                defaultSession.copy(state = SessionState.ENDED, currentCheck = Check.CHECKMATE)),

            ViewTest(::playView, "Play originated a stalemate",
                defaultSession.copy(state = SessionState.ENDED, currentCheck = Check.STALEMATE))
        ))
    }

    fun refreshViewTests(){
        testViews(listOf(
            ViewTest(::refreshView, "Refresh game without check and it's your turn",
                defaultSession.copy(state = SessionState.YOUR_TURN, currentCheck = Check.NO_CHECK)),

            ViewTest(::refreshView, "Refresh game with check",
                defaultSession.copy(state = SessionState.YOUR_TURN, currentCheck = Check.CHECK)),

            ViewTest(::refreshView, "Refresh game with checkmate",
                defaultSession.copy(state = SessionState.ENDED, currentCheck = Check.CHECKMATE)),

            ViewTest(::refreshView, "Refresh game with stalemate",
                defaultSession.copy(state = SessionState.ENDED, currentCheck = Check.STALEMATE))
        ))
    }
    
    fun movesViewTests(){
        testViews(listOf(
            ViewTest(::movesView, "Moves",
                defaultSession.copy(game = gameFromMoves("Pe2e4", "Pe7e5", "Nc3")))
        ))
    }

    fun helpViewTests(){
        testViews(listOf(
            ViewTest(::helpView, "Help",
                defaultSession)
        ))
    }

    private fun testViews(views: List<ViewTest>) {
        views.forEach {
            println("-----------------------------\nView start - ${it.name}\n-----------------------------")
            it.view(it.session)
            println("-----------------------------\nView end - ${it.name}\n-----------------------------")
            println("Press enter to show next view.")
            readLine()!!
            println("\n\n\n".repeat(10))
        }
    }
}

fun main(){
    ViewsTests.openViewTests()
    ViewsTests.joinViewTests()
    ViewsTests.playViewTests()
    ViewsTests.refreshViewTests()
    ViewsTests.movesViewTests()
    ViewsTests.helpViewTests()
}
