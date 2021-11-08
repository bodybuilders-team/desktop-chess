package commands

import Board


class Commands(private var board: Board): ChessCommands{
    override fun open(gameId: String): Result {
        TODO("Not yet implemented")
    }

    override fun join(gameId: String): Result {
        TODO("Not yet implemented")
    }

    override fun play(stringMove: String): Result {
        board = board.makeMove(stringMove)
        return Result.CONTINUE
    }

    override fun refresh(): Result {
        TODO("Not yet implemented")
    }

    override fun moves(): Result {
        TODO("Not yet implemented")
    }

    override fun exit(): Result {
        return Result.EXIT
    }

}
