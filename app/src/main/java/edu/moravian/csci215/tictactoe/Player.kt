package edu.moravian.csci215.tictactoe

/**
 * Represents a player in the tic-tac-toe game. This player could either be a human or an AI.
 */
interface Player {
    /**
     * The name of the player.
     */
    val name: String

    /**
     * Tells the player it can play it's turn.
     * @param game the game that is being played on
     * @param piece the piece that the player will play
     */
    fun startTurn(game: Game, piece: Char)
}
