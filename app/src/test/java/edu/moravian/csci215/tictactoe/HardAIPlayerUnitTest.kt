package edu.moravian.csci215.tictactoe

import org.junit.Test

/**
 * Local unit tests for hard AI. They are broken down into the different situations that the AI
 * looks for. Depends on the Board class so make sure that is working first.
 */
class HardAIPlayerUnitTest {
    /**
     * Check that the hard AI plays starts by playing the in the upper-left or middle spaces
     * depending on if it is first or second player (and where the first player played).
     */
    @Test
    fun play_firstMove() {
        val ai = HardAIPlayer()
        val board = Board()
        assertAIPlay(board, ai, 'O', 0, 0)
        assertAIPlay(board, ai, 'X', 1, 1)
        assertAIPlay("   | O |   ", ai, 'X', 0, 0)
    }

    /**
     * Check that the hard AI plays to win (even if they could block).
     */
    @Test
    fun play_win() {
        val ai = HardAIPlayer()

        // As first player O
        assertAIPlay("OO |XX |   ", ai, 'O', 0, 2) // row  (also a block to be performed that should be ignored)
        assertAIPlay("XOX| O |   ", ai, 'O', 2, 1) // column
        assertAIPlay("OXX| O |   ", ai, 'O', 2, 2) // diagonal

        // As second player X
        assertAIPlay("XX | OO|  O", ai, 'X', 0, 2) // row
        assertAIPlay("OOX| O |  X", ai, 'X', 1, 2) // column
        assertAIPlay("OOX|O  |X  ", ai, 'X', 1, 1) // diagonal
    }

    /**
     * Check that the hard AI plays to block the other player if they can't win.
     */
    @Test
    fun play_block() {
        val ai = HardAIPlayer()

        // As first player O
        assertAIPlay("XX |O  |O  ", ai, 'O', 0, 2) // row
        assertAIPlay("OOX|  X|   ", ai, 'O', 2, 2) // column
        assertAIPlay("OOX| X |   ", ai, 'O', 2, 0) // diagonal

        // As second player X
        assertAIPlay("OO |OXX|   ", ai, 'X', 0, 2) // row
        assertAIPlay("OXX|O  | O ", ai, 'X', 2, 0) // column
        assertAIPlay("O  |OOX|X  ", ai, 'X', 2, 2) // diagonal
    }

    /**
     * Check that the hard AI plays to the center when not winning, forking, or blocking.
     */
    @Test
    fun play_center() {
        val ai = HardAIPlayer()

        // As first player O
        // Will never happen when playing second (accept as during opening move)
        assertAIPlay("O  |   |  X", ai, 'O', 1, 1)
        assertAIPlay("O  |   | X ", ai, 'O', 1, 1)
        assertAIPlay("O  |  X|   ", ai, 'O', 1, 1)
        assertAIPlay("O X|   |   ", ai, 'O', 1, 1)
    }

    /**
     * Check that the hard AI plays to a corner opposite the opponent.
     */
    @Test
    fun play_oppositeCorner() {
        val ai = HardAIPlayer()

        // As first player O
        assertAIPlay("OX | O |XOX", ai, 'O', 0, 2)
        assertAIPlay("O X|XOO|  X", ai, 'O', 2, 0)

        // As second player X
        assertAIPlay("  O|OX |   ", ai, 'X', 2, 0)
        assertAIPlay("  O|OXX|  O", ai, 'X', 0, 0)
        assertAIPlay("  O|OXX|XOO", ai, 'X', 0, 0)
        assertAIPlay(" O | X |O  ", ai, 'X', 0, 2)
        assertAIPlay("O  | X | O ", ai, 'X', 2, 2)
    }

    /**
     * Check that the hard AI plays to any corner eventually.
     */
    @Test
    fun play_anyCorner() {
        val ai = HardAIPlayer()

        // As first player O
        assertAIPlay("O  | X |   ", ai, 'O', 0, 2)
        assertAIPlay("O  |XO |  X", ai, 'O', 0, 2)
        assertAIPlay("OX | O |  X", ai, 'O', 0, 2)
        assertAIPlay("OXO| X | OX", ai, 'O', 2, 0)

        // As second player X
        assertAIPlay("   | XO| O ", ai, 'X', 0, 0)
        assertAIPlay("   |OXO|   ", ai, 'X', 0, 0)
        assertAIPlay("  O| X |O  ", ai, 'X', 0, 0)
        assertAIPlay("O  | XO| OX", ai, 'X', 0, 2)
    }

    /**
     * Check that the hard AI plays finally plays in any spot.
     */
    @Test
    fun play_any() {
        val ai = HardAIPlayer()

        // As first player O
        assertAIPlay("O X|XOO|OXX", ai, 'O', 0, 1)
        assertAIPlay("OXO| OX|XOX", ai, 'O', 1, 0)
        assertAIPlay("OXO|XO |XOX", ai, 'O', 1, 2)
        assertAIPlay("OXX|XOO|O X", ai, 'O', 2, 1)

        // As second player X
        assertAIPlay("X O|OOX|X O", ai, 'X', 0, 1)
        assertAIPlay("XOX| O |OXO", ai, 'X', 1, 2)
    }
}
