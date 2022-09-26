package edu.moravian.csci215.tictactoe

import kotlin.random.Random

typealias Loc = Pair<Int, Int>

/**
 * A tic-tac-toe player that uses AI to play pieces on the board.
 *
 * This class is abstract and is the base class for all AIs. It provides various utility functions
 * that may be useful for various AIs.
 */
abstract class AIPlayer(override val name: String) : Player {
    /**
     * When an AI player starts its turn it immediately plays on the board.
     * @param game the game that is being played on
     * @param piece the piece that the player will play
     */
    override fun startTurn(game: Game, piece: Char) {
        val (r, c) = findMove(game.board, piece)
        game.playPiece(r, c)
    }

    /**
     * Have the AI find a place to play a piece.
     * @param board the board to play on
     * @param piece the piece to play (either 'X' or 'O')
     */
    abstract fun findMove(board: Board, piece: Char): Loc

    /**
     * Play the piece randomly onto the board. It will continually try to place the piece until it
     * succeeds.
     * @param board the board to play on
     * @return the location to play
     */
    protected fun playRandomMove(board: Board): Loc {
        var r = Random.Default.nextInt(3)
        var c = Random.Default.nextInt(3)
        var attempts = 0
        while (!board.emptyAt(r, c)) {
            r = Random.Default.nextInt(3)
            c = Random.Default.nextInt(3)
            if (attempts++ > 10000) {
                throw RuntimeException("playRandomMove() unable to succeed")
            }
        }
        return Loc(r, c)
    }

    /**
     * Finds an empty location on the board where placing the given piece would win the game. These
     * are locations where the piece is seen to be twice in a row, column, or diagonal with the
     * third space being empty.
     *
     * This may be used to either find a place where the AI can win or where the AI can block the
     * other player.
     *
     * @param board the board to be played on
     * @param piece the piece to play (either 'X' or 'O') that would cause the win
     * @return a 2-element list with the row and column of the empty location to get the win, or
     * null if no immediate wins are found
     */
    protected fun findWin(board: Board, piece: Char): Loc? =
        (0..8).map { Loc(it / 3, it % 3) }.firstOrNull { (r, c) -> isWinningLocation(board, r, c, piece) }

    /**
     * This checks if the given location is empty, and one of the following is true:
     * - the other two pieces in the row are equal to the given piece
     * - the other two pieces in the column are equal to the given piece
     * - the piece is along a diagonal and the other two pieces in the diagonal are equal to the
     * given piece
     *
     * @param board the board to be played on
     * @param r the row which would receive the piece
     * @param c the column which would receive the piece
     * @param piece the piece to play (either 'X' or 'O') that would cause the win
     * @return true if placing the given piece at the given location is allowed and doing so would
     * win the game for that player
     */
    private fun isWinningLocation(board: Board, r: Int, c: Int, piece: Char): Boolean =
        board.emptyAt(r, c) && ( // destination must be empty
            ((0..2).all { it == r || board[it, c] == piece }) || // check the row
                ((0..2).all { it == c || board[r, it] == piece }) || // check the column
                (r == c && (0..2).all { it == r || board[it, it] == piece }) || // check the forward diagonal
                (r == 2 - c && (0..2).all { it == r || board[it, 2 - it] == piece }) // check the reverse diagonal
            )

    /**
     * Gets the opposite piece, thus if given 'X' return 'O' and vice-versa.
     * @param piece either 'X' or 'O'
     * @return the opposite: 'O' or 'X'
     */
    protected fun oppositePiece(piece: Char): Char = if (piece == 'X') 'O' else 'X'
}

/**
 * The easy AI always plays pieces randomly.
 */
class EasyAIPlayer : AIPlayer("Easy AI") {
    /**
     * The easy AI always plays pieces randomly.
     * @param board the board to play on
     * @param piece the piece to play (either 'X' or 'O')
     */
    override fun findMove(board: Board, piece: Char) = playRandomMove(board)
}

/**
 * The medium AI tries the following plays in this order:
 * - that will win for the AI
 * - that will block the opponent from winning
 * - randomly
 */
class MediumAIPlayer : AIPlayer("Medium AI") {
    /**
     * Tries to find a location to win, then a location to block, and finally chooses randomly.
     * @param board the board to play on
     * @param piece the piece to play (either 'X' or 'O')
     */
    override fun findMove(board: Board, piece: Char): Loc =
        findWin(board, piece) ?: findWin(board, oppositePiece(piece)) ?: playRandomMove(board)
}

/**
 * The hard AI tries the following plays in this order:
 * - if the first move: top-left if first player or center already taken, otherwise center
 * - that will win for the AI
 * - that will block the opponent from winning
 * - in the center
 * - in a corner opposite the opponent
 * - in any corner
 * - in any (edge) spot
 * See https://en.wikipedia.org/wiki/Tic-tac-toe#Strategy (note: the forking checks are not actually
 * necessary as they are covered by the other steps)
 */
class HardAIPlayer : AIPlayer("Hard AI") {
    /**
     * Tries to find a location to win, then a location to block, the center location, a corner
     * location opposite the opponent, any empty corner, any remaining (edge) location. To make the
     * first moves easier, they are hard-coded (if first player then top-left corner, if second
     * player then center or top-left corner).
     *
     * See https://en.wikipedia.org/wiki/Tic-tac-toe#Strategy (note: the forking checks are not
     * actually necessary as they are covered by the other steps)
     *
     * @param board the board to play on
     * @param piece the piece to play (either 'X' or 'O')
     */
    override fun findMove(board: Board, piece: Char): Loc {
        val opponent: Char = oppositePiece(piece)

        // First move is fixed
        if (board.countPieces(piece) == 0) {
            // Top-left if first player or center already taken, otherwise center
            return if (board.countPieces(opponent) != 0 && board.emptyAt(1, 1)) Loc(1, 1) else Loc(0, 0)
        }

        // Find a win or block a win
        val location = findWin(board, piece) ?: findWin(board, opponent)
        if (location != null) return location

        // Play in the center (not actually needed since it is covered by the opening moves)
        if (board.emptyAt(1, 1)) return Loc(1, 1)

        // Try to play in a corner opposite of the opponent, then in any corner, then anywhere
        val emptyCorners = CORNERS.filter { (r, c) -> board.emptyAt(r, c) }
        return emptyCorners.firstOrNull { (r, c) -> board[2 - r, 2 - c] == opponent } ?: // Play corners opposite the opponent
            emptyCorners.firstOrNull() ?: // Play in any corner
            EDGES.first { (r, c) -> board.emptyAt(r, c) } // Play on any edge (only places left)
    }

    companion object {
        /** Corner locations on the board  */
        private val CORNERS = arrayOf(Loc(0, 0), Loc(0, 2), Loc(2, 2), Loc(2, 0))

        /** Edge locations on the board (not including corners)  */
        private val EDGES = arrayOf(Loc(0, 1), Loc(1, 2), Loc(2, 1), Loc(1, 0))
    }
}
