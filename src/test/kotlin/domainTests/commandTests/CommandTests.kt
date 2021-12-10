package domainTests.commandTests

import domain.*
import GameStateStub
import domain.board.*
import domain.commands.*
import domain.move.Move
import domain.pieces.Army
import kotlin.test.*


class CommandTests {
    
    //ExitCommand

    @Test
    fun `Exit command returns failure`() {
        assertTrue(ExitCommand().invoke().isFailure)
    }

    //HelpCommand

    @Test
    fun `Help command doesn't make any changes to game`() {
        val session = Session("test", SessionState.YOUR_TURN, Army.WHITE, Board(), emptyList(), Check.NO_CHECK)
        val result = HelpCommand(session).invoke()

        assertTrue(result.isSuccess)
        assertEquals(session, result.getOrThrow())
    }

    //JoinCommand

    @Test
    fun `Join command joins the player to an existing game if it exists`(){
        val gameName = "test"
        
        val db = GameStateStub()
        db.createGame(gameName)

        val move = Move("Pe2e4")
        db.postMove(gameName, move)

        val result = JoinCommand(db).execute(gameName)
        val chess = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals(gameName, chess.name)
        assertEquals(listOf(move), chess.moves)
    }

    @Test
    fun `Join command game state is ENDED if there's a checkmate in board of existing game`(){
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)

        //Fool's mate! 🤡 - https://www.chess.com/article/view/fastest-chess-checkmates
        val movesForFoolsMate = gameFromMoves("f3", "e5", "g4", "Qh4").second

        movesForFoolsMate.forEach { db.postMove(gameName, it) }

        val result = JoinCommand(db).execute(gameName)
        val chess = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals(gameName, chess.name)
        assertEquals(movesForFoolsMate, chess.moves)
        assertEquals(SessionState.ENDED, chess.state)
    }

    @Test
    fun `Join command game state is ENDED if there's a stalemate in board of existing game`(){
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)

        //Fastest stalemate known - https://www.chess.com/forum/view/game-showcase/fastest-stalemate-known-in-chess
        val movesForFastestStalemate =
            gameFromMoves("e3", "a5", "Qh5", "Ra6", "Qa5", "h5", "2h4", "Rah6", "Qc7", "f6", "Qd7", "Kf7", "Qb7",
                "Qd3", "Qb8", "Qh7", "Qc8", "Kg6", "Qe6").second

        movesForFastestStalemate.forEach { db.postMove(gameName, it) }

        val result = JoinCommand(db).execute(gameName)
        val chess = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals(gameName, chess.name)
        assertEquals(movesForFastestStalemate, chess.moves)
        assertEquals(SessionState.ENDED, chess.state)
    }

    @Test
    fun `Join command to a game that doesn't exist throws`(){
        val db = GameStateStub()

        assertFailsWith<CommandException> {
            JoinCommand(db).execute("test")
        }
    }
    
    @Test
    fun `Join command missing game name throws`(){
        val db = GameStateStub()

        assertFailsWith<CommandException> {
            JoinCommand(db).execute(null)
        }
    }
    
    //TODO("Join command session current check")
    
    //MovesCommand
    
    
    
    //OpenCommand

    @Test
    fun `Open command creates game if it doesn't exist`(){
        val db = GameStateStub()
        val result = OpenCommand(db).execute("test")
        val chess = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals("test", chess.name)
        assertEquals(listOf(), chess.moves)
    }

    @Test
    fun `Open command opens an existing game if it exists`(){
        val gameName = "test"
        
        val db = GameStateStub()
        db.createGame(gameName)

        val move = Move("Pe2e4")
        db.postMove(gameName, move)

        val result = OpenCommand(db).execute(gameName)
        val chess = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals(gameName, chess.name)
        assertEquals(listOf(move), chess.moves)
    }

    @Test
    fun `Open command game state is ENDED if there's a checkmate in board of already existing game`(){
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)
        
        //Fool's mate! 🤡 - https://www.chess.com/article/view/fastest-chess-checkmates
        val movesForFoolsMate = gameFromMoves("f3", "e5", "g4", "Qh4").second
        
        movesForFoolsMate.forEach { db.postMove(gameName, it) }

        val result = OpenCommand(db).execute(gameName)
        val chess = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals(gameName, chess.name)
        assertEquals(movesForFoolsMate, chess.moves)
        assertEquals(SessionState.ENDED, chess.state)
    }

    @Test
    fun `Open command game state is ENDED if there's a stalemate in board of already existing game`(){
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)

        //Fastest stalemate known - https://www.chess.com/forum/view/game-showcase/fastest-stalemate-known-in-chess
        val movesForFastestStalemate =
            gameFromMoves("e3", "a5", "Qh5", "Ra6", "Qa5", "h5", "2h4", "Rah6", "Qc7", "f6", "Qd7", "Kf7", "Qb7",
                                "Qd3", "Qb8", "Qh7", "Qc8", "Kg6", "Qe6").second

        movesForFastestStalemate.forEach { db.postMove(gameName, it) }

        val result = OpenCommand(db).execute(gameName)
        val chess = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals(gameName, chess.name)
        assertEquals(movesForFastestStalemate, chess.moves)
        assertEquals(SessionState.ENDED, chess.state)
    }

    @Test
    fun `Open command missing game name throws`(){
        val db = GameStateStub()

        assertFailsWith<CommandException> {
            OpenCommand(db).execute(null)
        }
    }

    //TODO("Open command session current check")

    //PlayCommand

    @Test
    fun `Play command returns success`() {
        val db = GameStateStub()
        db.createGame("test")

        val session = Session("test", SessionState.YOUR_TURN, Army.WHITE, Board(), emptyList(), Check.NO_CHECK)

        val move = "Pe2e4"
        val result = PlayCommand(db, session).execute(move)
        val chess = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals(SessionState.WAITING_FOR_OPPONENT, chess.state)
        assertEquals(listOf(Move.validated(move, session.board, session.moves)), chess.moves)
    }

    //RefreshCommand
    
    @Test
    fun `Refresh command returns success`() {
        val db = GameStateStub()
        db.createGame("test")

        val session = Session("test", SessionState.WAITING_FOR_OPPONENT, Army.BLACK, Board(), emptyList(), Check.NO_CHECK)
        val move = Move("Pe2e4")
        db.postMove("test", move)

        val result = RefreshCommand(db, session).invoke()
        val chess = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals(SessionState.YOUR_TURN, chess.state)
        assertEquals(listOf(move), chess.moves)
    }

    @Test
    fun `Moves command returns success`() {
        val db = GameStateStub()
        db.createGame("test")

        var session = Session("test", SessionState.WAITING_FOR_OPPONENT, Army.WHITE, Board(), emptyList(), Check.NO_CHECK)
        val move = Move("Pe2e4")
        db.postMove("test", move)

        session = RefreshCommand(db, session).invoke().getOrThrow()
        val result = MovesCommand(db, session).invoke()

        assertTrue(result.isSuccess)
        assertEquals(listOf(move), result.getOrThrow().moves)
    }
    
    //boardWithMoves

    @Test
    fun `boardWithMoves returns original board if moves list is empty`() {
        val board = Board()
        assertEquals(board.toString(), boardWithMoves(emptyList()).toString())
    }

    @Test
    fun `boardWithMoves returns new board with the moves made`() {
        val board = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                "p pppppp" +
                "        " +
                " p      " +
                "    P   " +
                "  N     " +
                "PPPP PPP" +
                "R BQKBNR"
            )
        )
        val moves = listOf(Move("Pe2e4"), Move("Pb7b5"), Move("Nb1c3"))
        
        assertEquals(board.toString(), boardWithMoves(moves).toString())
    }
}
