package edu.moravian.csci215.tictactoe

/**
 * A tic-tac-toe player that gets its input from a human.
 */
data class HumanPlayer(override val name: String) : Player {
    /**
     * Tells the player it can play it's turn.
     * For humans this does nothing, the activity will deal with this.
     * @param game the game that is being played on
     * @param piece the piece that the player will play
     */
    override fun startTurn(game: Game, piece: Char) { }
}
