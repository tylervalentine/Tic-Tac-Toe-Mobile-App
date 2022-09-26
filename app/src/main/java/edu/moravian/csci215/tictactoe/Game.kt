package edu.moravian.csci215.tictactoe

import androidx.lifecycle.ViewModel

/**
 * The model of the entire game. The wraps up several rounds of the game into a single model.
 */
class Game: ViewModel() {
    /** The possible game results, each has a unique integer value. */
    enum class Result(val value: Int) {
        TIE(-1), PLAYER_1_WIN(0), PLAYER_2_WIN(1)
    }

    /** The players playing the game */
    var players = emptyList<Player>()
        private set

    /** The pieces for the players in the current round */
    var pieces = emptyList<Char>()
        private set

    /** The board the game is being played on */
    val board = Board()

    /** Whose turn it is (index of players); -1 if not in the middle of a round */
    var turn = -1

    /** The number of wins by player 1 */
    var wins1 = 0
        private set

    /** The number of wins by player 2 */
    var wins2 = 0
        private set

    /** The number of ties between the players */
    var ties = 0
        private set

    /** True if the game has started. No rounds may have started though. */
    val hasStarted: Boolean
        get() = players.isNotEmpty()

    /** True if the we are currently in the middle of a round. */
    val roundInProgress: Boolean
        get() = hasStarted && turn != -1

    /**
     * The function to call when a round ends. The function takes the result of the game, one of the
     * values Game.Result.TIE, Game.Result.PLAYER_1_WINS, or Game.Result.PLAYER_2_WINS.
     */
    var onRoundOverListener: ((Result) -> Unit)? = null

    /**
     * Start a game between the two players.
     * @param player1 one of the players, may not be the first to play though
     * @param player2 the other player
     */
    fun startGame(player1: Player, player2: Player) {
        require (!hasStarted) { "game already started" }
        players = listOf(player1, player2)
    }

    /**
     * Start a new round of the game.
     * This resets the game board, chooses a first player, and sets the player pieces. Informs the
     * first player to play.
     */
    fun startNewRound() {
        require(hasStarted) { "game has not started" }
        require(!roundInProgress) { "game round currently in progress" }
        board.reset()
        turn = if (Math.random() < 0.5) 0 else 1
        pieces = if (turn == 0) listOf('X', 'O') else listOf('O', 'X')
        players[turn].startTurn(this, pieces[turn])
    }

    /**
     * Play a piece on the board. This is only allowed if a round is currently in-progress.
     *
     * This should only be called for the current player.
     *
     * @return Pair of the Board at the end of the game and an integer of 0 if the first player won,
     * 1 if the second player won, and -1 if it was a tie.
     */
    fun playPiece(r: Int, c: Int): Boolean {
        require(roundInProgress) { "game round currently not in progress" }
        if (!board.playPiece(r, c, pieces[turn])) return false
        if (board.isGameOver) {
            // Round is over
            turn = -1 // marks the round over
            val result = if (board.hasWon(pieces[0])) { // determine game result
                wins1++
                Result.PLAYER_1_WIN
            } else if (board.hasWon(pieces[1])) {
                wins2++
                Result.PLAYER_2_WIN
            } else {
                ties++
                Result.TIE
            }
            onRoundOverListener?.invoke(result)
        } else {
            // Start the other player's turn
            turn = 1 - turn // switch turns
            players[turn].startTurn(this, pieces[turn])
        }
        return true
    }

    // /////// General Object Methods //////////
    /* TODO: override fun toString(): String {
        return "Game{" +
            "players=" + players.contentToString() +
            ", wins1=" + wins1 +
            ", wins2=" + wins2 +
            ", ties=" + ties +
            '}'
    }*/
}
