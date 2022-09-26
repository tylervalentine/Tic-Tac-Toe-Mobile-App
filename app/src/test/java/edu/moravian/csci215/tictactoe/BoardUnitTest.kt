package edu.moravian.csci215.tictactoe

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Test all of the Board method. Many of these are inter-related so frequently these test methods
 * don't test a single method but instead test several at a time.
 */
class BoardUnitTest {
    /**
     * Tests the basics of get()/[], emptyAt(), playPiece(), and reset(). All other tests will end
     * up testing these as well.
     */
    @Test
    fun basics() {
        val board = Board()
        assertAllCoords { r, c -> board[r, c] == ' ' }
        assertAllCoords { r, c -> board.emptyAt(r, c) }
        assertTrue(board.playPiece(0, 0, 'O'))
        assertAllCoords { r, c -> board[r, c] == (if (r == 0 && c == 0) 'O' else ' ') }
        assertAllCoords { r, c -> (r == 0 && c == 0) == !board.emptyAt(r, c) }
        assertTrue(board.playPiece(0, 1, 'X'))
        for (r in 0..2) {
            for (c in 0..2) {
                if (r == 0 && c == 0) {
                    assertFalse(board.emptyAt(r, c))
                    assertEquals('O', board[r, c])
                } else if (r == 0 && c == 1) {
                    assertFalse(board.emptyAt(r, c))
                    assertEquals('X', board[r, c])
                } else {
                    assertTrue(board.emptyAt(r, c))
                    assertEquals(' ', board[r, c])
                }
            }
        }
        assertTrue(board.playPiece(0, 2, 'O'))
        for (r in 0..2) {
            for (c in 0..2) {
                if (r == 0) {
                    assertFalse(board.emptyAt(r, c))
                    when (c) {
                        0 -> assertEquals('O', board[r, c])
                        1 -> assertEquals('X', board[r, c])
                        else -> assertEquals('O', board[r, c]) // c == 2
                    }
                } else {
                    assertTrue(board.emptyAt(r, c))
                    assertEquals(' ', board[r, c])
                }
            }
        }
        board.reset()
        assertAllCoords { r, c -> board[r, c] == ' ' }
        assertAllCoords { r, c -> board.emptyAt(r, c) }
    }

    /**
     * Tests the copy method and makes sure it actually copies.
     */
    @Test
    fun copy() {
        val board = Board()
        board.playPiece(0, 0, 'O')
        board.playPiece(0, 1, 'X')
        board.playPiece(1, 1, 'O')
        board.playPiece(2, 2, 'X')
        board.playPiece(1, 0, 'O')
        val board2 = board.copy()
        assertAllCoords { r, c -> board[r, c] == board2[r, c] }
        assertEquals(board, board2)

        // make sure it is a copy, not just a reference
        board2.playPiece(2, 1, 'X')
        assertAllCoords { r, c -> (r == 2 && c == 1) != (board[r, c] == board2[r, c]) }
        assertNotEquals(board, board2)
    }

    /**
     * Tests countPieces().
     */
    @Test
    fun countPieces() {
        val board = Board()
        assertPieceCounts(board, 0, 0)
        assertTrue(board.playPiece(0, 0, 'O'))
        assertPieceCounts(board, 0, 1)
        assertTrue(board.playPiece(0, 1, 'X'))
        assertPieceCounts(board, 1, 1)
        assertTrue(board.playPiece(1, 1, 'O'))
        assertPieceCounts(board, 1, 2)
        assertTrue(board.playPiece(2, 2, 'X'))
        assertPieceCounts(board, 2, 2)
        assertTrue(board.playPiece(1, 0, 'O'))
        assertPieceCounts(board, 2, 3)
        assertTrue(board.playPiece(2, 0, 'X'))
        assertPieceCounts(board, 3, 3)
        assertTrue(board.playPiece(2, 1, 'O'))
        assertPieceCounts(board, 3, 4)
        assertTrue(board.playPiece(1, 2, 'X'))
        assertPieceCounts(board, 4, 4)
        assertTrue(board.playPiece(0, 2, 'O'))
        assertPieceCounts(board, 4, 5)
    }

    /**
     * Tests playPiece() including playing on top of another piece or after the end of the game.
     */
    @Test
    fun playPiece() {
        val board = Board()
        assertTrue(board.playPiece(0, 0, 'O'))
        assertFalse(board.playPiece(0, 0, 'O'))
        assertFalse(board.playPiece(0, 0, 'X'))
        assertTrue(board.playPiece(1, 1, 'X'))
        assertFalse(board.playPiece(0, 0, 'O'))
        assertFalse(board.playPiece(0, 0, 'X'))
        assertFalse(board.playPiece(1, 1, 'O'))
        assertFalse(board.playPiece(1, 1, 'X'))
        assertTrue(board.playPiece(0, 1, 'O'))
        assertFalse(board.playPiece(0, 1, 'X'))
        assertTrue(board.playPiece(1, 0, 'X'))
        assertFalse(board.playPiece(1, 0, 'X'))
        assertTrue(board.playPiece(0, 2, 'O')) // wins the game for O
        assertFalse(board.playPiece(0, 2, 'X'))
        assertFalse(board.playPiece(2, 2, 'X'))
        assertFalse(board.playPiece(1, 2, 'X'))
        assertFalse(board.playPiece(2, 1, 'X'))
        assertFalse(board.playPiece(2, 0, 'X'))
        val board2 = Board.createFromString("OXX| O |  O") // a different winning board
        assertFalse(board2.playPiece(1, 0, 'X'))
        assertFalse(board2.playPiece(1, 2, 'X'))
        assertFalse(board2.playPiece(0, 2, 'X'))
    }

    /**
     * Tests hasWon, hasTied, and isGameOver with various winning, tied, and incomplete boards.
     */
    @Test
    fun isGameOver() {
        // Look at an entire (quick) game
        val board = Board()
        assertGameOver(board, xWon = false, oWon = false, tied = false)
        assertTrue(board.playPiece(0, 0, 'O'))
        assertGameOver(board, xWon = false, oWon = false, tied = false)
        assertTrue(board.playPiece(0, 1, 'X'))
        assertGameOver(board, xWon = false, oWon = false, tied = false)
        assertTrue(board.playPiece(1, 1, 'O'))
        assertGameOver(board, xWon = false, oWon = false, tied = false)
        assertTrue(board.playPiece(1, 0, 'X'))
        assertGameOver(board, xWon = false, oWon = false, tied = false)
        assertTrue(board.playPiece(2, 2, 'O'))
        assertGameOver(board, xWon = false, oWon = true, tied = false)
        assertFalse(board.playPiece(2, 1, 'X'))

        // Look at a tie (and right before the tie)
        assertGameOver("OXO|OXO|XO ", xWon = false, oWon = false, tied = false)
        assertGameOver("OXO|OXO|XOX", xWon = false, oWon = false, tied = true)

        // Look at lots of ways to win
        assertGameOver("OOX|OX |X  ", xWon = true, oWon = false, tied = false) // diagonal
        assertGameOver("OXX| OX|  O", xWon = false, oWon = true, tied = false) // diagonal
        assertGameOver("OXX|O  |O  ", xWon = false, oWon = true, tied = false) // column
        assertGameOver("OOX|O X|  X", xWon = true, oWon = false, tied = false) // column
        assertGameOver("OO |XXX|O  ", xWon = true, oWon = false, tied = false) // row
        assertGameOver("   |XXO|OOO", xWon = false, oWon = true, tied = false) // row
    }
}
