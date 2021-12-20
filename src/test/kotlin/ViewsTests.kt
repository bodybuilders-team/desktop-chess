import domain.game.*
import domain.Session
import domain.SessionState
import domain.pieces.Army
import ui.console.*


object ViewsTests {
    private val defaultGameResultingInBlackCheck = gameFromMoves("c3", "d6", "a3", "e6", "Qa4")
    private val defaultGameResultingInWhiteCheck = gameFromMoves("d3", "c6", "a3", "Qa5")
    private val defaultGameResultingInCheckMate = gameFromMoves("f3", "e5", "g4", "Qh4")
    private val defaultGameResultingInStaleMate = gameFromMoves(
            "e3", "a5", "Qh5", "Ra6", "Qa5", "h5", "h4", "Rah6", "Qc7", "f6", "Qd7", "Kf7", "Qb7",
            "Qd3", "Qb8", "Qh7", "Qc8", "Kg6", "Qe6"
    )

    data class ViewTest(val view: View, val name: String, val session: Session)

    private val defaultSession = Session(name = "test", SessionState.YOUR_TURN, Army.WHITE, gameFromMoves())
    
    fun openViewTests(){
        testViews(listOf(
            ViewTest(::openView, "Open game without check",
                defaultSession.copy(state = SessionState.YOUR_TURN, game = gameFromMoves())),

            ViewTest(::openView, "Open game with enemy in check",
                defaultSession.copy(state = SessionState.WAITING_FOR_OPPONENT, game = defaultGameResultingInBlackCheck)),

            ViewTest(::openView, "Open game with your army in check",
                defaultSession.copy(state = SessionState.YOUR_TURN, game = defaultGameResultingInWhiteCheck)),

            ViewTest(::openView, "Open game with checkmate",
                defaultSession.copy(state = SessionState.ENDED, game = defaultGameResultingInCheckMate)),

            ViewTest(::openView, "Open game with stalemate",
                defaultSession.copy(state = SessionState.ENDED, game = defaultGameResultingInStaleMate))

            /*
            TODO Ties
            ViewTest(::openView, "Open game with dead position",
                defaultSession.copy(state = SessionState.ENDED, game = defaultGameResultingInDeadPosition)),
            
            ViewTest(::openView, "Open game with three-fold repetition",
                defaultSession.copy(state = SessionState.ENDED, game = defaultGameResultingInThreeFoldRepetition))
            */
        ))
    }

    fun joinViewTests(){
        testViews(listOf(
            ViewTest(::joinView, "Join game without check",
                defaultSession.copy(state = SessionState.YOUR_TURN, game = gameFromMoves())),

            ViewTest(::joinView, "Join game with your army  in check",
                defaultSession.copy(state = SessionState.YOUR_TURN, game = defaultGameResultingInBlackCheck)),

            ViewTest(::joinView, "Join game with enemy in check",
                defaultSession.copy(state = SessionState.WAITING_FOR_OPPONENT, game = defaultGameResultingInWhiteCheck)),

            ViewTest(::joinView, "Join game with checkmate",
                defaultSession.copy(state = SessionState.ENDED, game = defaultGameResultingInCheckMate)),

            ViewTest(::joinView, "Join game with stalemate",
                defaultSession.copy(state = SessionState.ENDED, game = defaultGameResultingInStaleMate)),

            /*
            TODO Ties
            ViewTest(::joinView, "Join game with dead position",
                defaultSession.copy(state = SessionState.ENDED, game = defaultGameResultingInDeadPosition)),
            
            ViewTest(::joinView, "Join game with three-fold repetition",
                defaultSession.copy(state = SessionState.ENDED, game = defaultGameResultingInThreeFoldRepetition))
            */
        ))
    }

    fun playViewTests(){
        testViews(listOf(
            ViewTest(::playView, "Play didn't originate check",
                defaultSession.copy(state = SessionState.WAITING_FOR_OPPONENT, game = gameFromMoves("Pe2e4"))),

            ViewTest(::playView, "Play originated a check",
                defaultSession.copy(state = SessionState.WAITING_FOR_OPPONENT, game = defaultGameResultingInBlackCheck)),

            ViewTest(::playView, "Play originated a checkmate",
                defaultSession.copy(state = SessionState.ENDED, game = defaultGameResultingInCheckMate)),

            ViewTest(::playView, "Play originated a stalemate",
                defaultSession.copy(state = SessionState.ENDED, game = defaultGameResultingInStaleMate))
        
            /*
            TODO Ties
            ViewTest(::playView, "Play originated a dead position",
                defaultSession.copy(state = SessionState.ENDED, game = defaultGameResultingInDeadPosition)),
            ViewTest(::playView, "Play originated a three-fold repetition",
                defaultSession.copy(state = SessionState.ENDED, game = defaultGameResultingInThreeFoldRepetition))
            */
        ))
    }

    fun refreshViewTests(){
        testViews(listOf(
            ViewTest(::refreshView, "Refresh game without check and it's your turn",
                defaultSession.copy(state = SessionState.YOUR_TURN, game = gameFromMoves("Pe2e4"))),

            ViewTest(::refreshView, "Refresh game with check",
                defaultSession.copy(state = SessionState.YOUR_TURN, game = defaultGameResultingInBlackCheck)),

            ViewTest(::refreshView, "Refresh game with checkmate",
                defaultSession.copy(state = SessionState.ENDED, game = defaultGameResultingInCheckMate)),

            ViewTest(::refreshView, "Refresh game with stalemate",
                defaultSession.copy(state = SessionState.ENDED, game = defaultGameResultingInStaleMate))

            /*
            TODO Ties
            ViewTest(::refreshView, "Refresh game with dead position",
                defaultSession.copy(state = SessionState.ENDED, game = defaultGameResultingInDeadPosition)),
            ViewTest(::refreshView, "Refresh with a three-fold repetition",
                defaultSession.copy(state = SessionState.ENDED, game = defaultGameResultingInThreeFoldRepetition))
            */
        ))
    }
    
    fun movesViewTests(){
        testViews(listOf(
            ViewTest(::movesView, "Moves",
                defaultSession.copy(game = defaultGameResultingInCheckMate))
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
            println("-----------------------------\nView start - ${it.name}\n-----------------------------\n")
            it.view(it.session)
            println("\n-----------------------------\nView end - ${it.name}\n-----------------------------")
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
