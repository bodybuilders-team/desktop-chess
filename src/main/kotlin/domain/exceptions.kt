package domain

class IllegalMoveException(val move: String, message: String) : Exception(message)
