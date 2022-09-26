package edu.moravian.csci215.tictactoe

/**
 * A Tic-Tac-Toe game board. It is always 3x3 and contains the chars ' ' (space) for an empty spot,
 * 'X' and 'O' for player moves.
 */
data class Board(private val board: Array<CharArray> = Array(3) { CharArray(3) { ' ' } }) {
    // Companion objects are typically used for static functions and variables
    companion object {
        val COLUMNS = charArrayOf('A', 'B', 'C')
        val ROWS = charArrayOf('1', '2', '3')

        /**
         * Converts a String representation of a board to an actual board. The String uses X and O
         * for pieces, spaces for empty places, and | to indicate the breaks between rows. The
         * String can be missing trailing empty spaces on each row.
         * @param boardStr the string representation of the board
         * @return the actual Board object
         */
        fun createFromString(boardStr: String): Board {
            val b = Board()
            var r = 0
            var c = 0
            for (piece in boardStr.uppercase()) {
                if (piece == '|') {
                    r++
                    c = 0
                } else {
                    require(piece == 'X' || piece == 'O' || piece == ' ') { "piece" }
                    b.board[r][c] = piece
                    c++
                }
            }
            return b
        }
    }

    /**
     * Get the piece at a specific location.
     * Note: the operator word here simply means that this will allow code to use board[r, c]
     * instead of board.get(r, c)
     * @param r the row, should be 0 to 2
     * @param c the column, should be 0 to 2
     * @return the piece character, one of 'X', 'O', or ' ' (if unoccupied)
     */
    operator fun get(r: Int, c: Int): Char = board[r][c]

    /**
     * Checks if the given location is empty on the board.
     * @param r the row, should be 0 to 2
     * @param c the column, should be 0 to 2
     * @return true if the location is unoccupied on the board, false otherwise
     */
    fun emptyAt(r: Int, c: Int): Boolean = this[r, c] == ' '

    /**
     * Returns the number of pieces of the given type across the entire board.
     * @param piece the piece to check for, one of ' ', 'X', or 'O'
     * @return the number of those pieces on the board
     */
    fun countPieces(piece: Char): Int =
        board.sumOf { row -> row.count { it == piece } }

    /**
     * Play a piece onto the board, checking to make sure that the space is empty first.
     * @param r the row where to place the piece (0-2)
     * @param c the column where to place the piece (0-2)
     * @param piece the piece to place, either 'X' or 'O'
     * @return true if the space was empty and the play was successful, false otherwise
     */
    fun playPiece(r: Int, c: Int, piece: Char): Boolean {
        require(piece == 'X' || piece == 'O') { "piece" }
        if (isGameOver || board[r][c] != ' ') {
            return false
        }
        board[r][c] = piece
        return true
    }

    /**
     * Returns true if a column is filled with only the given piece
     * @param c the column to check
     * @param piece the piece being checked (either 'X' or 'O')
     * @return true if every piece in the column is equal to the given piece
     */
    private fun checkColumn(c: Int, piece: Char): Boolean =
        board[0][c] == piece && board[1][c] == piece && board[2][c] == piece

    /**
     * Returns true if a row is filled with only the given piece
     * @param r the row to check
     * @param piece the piece being checked (either 'X' or 'O')
     * @return true if every piece in the row is equal to the given piece
     */
    private fun checkRow(r: Int, piece: Char): Boolean =
        board[r][0] == piece && board[r][1] == piece && board[r][2] == piece

    /**
     * Check if either of the two diagonals are completely equal to the given piece.
     * @param piece the piece being checked (either 'X' or 'O')
     * @return true if either of the two diagonals is entirely equal to the given piece
     */
    private fun checkDiagonals(piece: Char): Boolean =
        board[1][1] == piece && (
            board[0][0] == piece && board[2][2] == piece ||
                board[0][2] == piece && board[2][0] == piece
            )

    /**
     * @param piece the piece to check for a win with
     * @return true if the player with the given piece has won, false otherwise
     */
    fun hasWon(piece: Char): Boolean =
        (0..2).any { checkColumn(it, piece) } || (0..2).any { checkRow(it, piece) } || checkDiagonals(piece)

    /**
     * true if the board is full of pieces (no ' ' pieces on the board) and neither X or O has won
     */
    val hasTied: Boolean
        get() = countPieces(' ') == 0 && !hasWon('X') && !hasWon('O')

    /**
     * true if X or O has won or the game is tied
     */
    val isGameOver: Boolean
        get() = hasWon('X') || hasWon('O') || countPieces(' ') == 0

    /**
     * Reset the board to all empty spaces
     */
    fun reset() = board.forEach { it.fill(' ') }

    /**
     * Prints the board nicely
     */
    fun print() {
        println(COLUMNS.joinToString("|", " |", "|"))
        println("-+-+-+-+")
        for ((num, row) in ROWS.zip(board)) {
            println(row.joinToString("|", "$num|", "|"))
            println("-+-+-+-+")
        }
    }

    // /////// General Object Methods //////////
    // Usually Kotlin provides these for data classes, but the provided ones don't work with arrays
    override fun toString(): String = "Board(board=" + board.contentDeepToString() + ")"
    override fun equals(other: Any?): Boolean =
        this === other || javaClass == other?.javaClass && board.contentDeepEquals((other as Board).board)
    override fun hashCode(): Int = board.contentDeepHashCode()
    fun copy(): Board = Board(board.map { it.copyOf(3) }.toTypedArray())
}
