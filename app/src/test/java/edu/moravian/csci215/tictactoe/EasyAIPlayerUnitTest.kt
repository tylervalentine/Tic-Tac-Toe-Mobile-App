package edu.moravian.csci215.tictactoe

import org.junit.Test

/**
 * Local unit tests for easy AI. Depends on the Board class so make sure that is working first.
 */
class EasyAIPlayerUnitTest {
    @Test
    fun play() {
        val ai = EasyAIPlayer()
        var piece = 'O'
        var board = assertAIPlaysRandomly(Board(), ai, piece)
        while (!board.isGameOver) {
            piece = if (piece == 'O') 'X' else 'O' // switch player
            board = assertAIPlaysRandomly(board, ai, piece)
        }
    }
}
