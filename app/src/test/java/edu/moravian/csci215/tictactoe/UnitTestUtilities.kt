package edu.moravian.csci215.tictactoe

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

/**
 * Assert that the board has the given counts for Xs and Os (and spaces).
 * @param board the board to check
 * @param x the expected number of Xs
 * @param o the expected number of Os
 */
fun assertPieceCounts(board: Board, x: Int, o: Int) {
    assertEquals(x, board.countPieces('X'))
    assertEquals(o, board.countPieces('O'))
    assertEquals(9 - x - o, board.countPieces(' '))
}

/**
 * Assert if the board is in a game-over state and won has won or tied depending on the
 * parameters.
 * @param board the board to check, to be converted with createBoardFromString()
 * @param xWon if X has won on the given board
 * @param oWon if O has won on the given board
 * @param tied if there is a tie
 */
fun assertGameOver(board: String, xWon: Boolean, oWon: Boolean, tied: Boolean) =
    assertGameOver(Board.createFromString(board), xWon, oWon, tied)

/**
 * Assert if the board is in a game-over state and won has won or tied depending on the
 * parameters.
 * @param board the board to check
 * @param xWon if X has won on the given board
 * @param oWon if O has won on the given board
 * @param tied if there is a tie
 */
fun assertGameOver(board: Board, xWon: Boolean, oWon: Boolean, tied: Boolean) {
    assertEquals(xWon, board.hasWon('X'))
    assertEquals(oWon, board.hasWon('O'))
    assertEquals(tied, board.hasTied)
    assertEquals(xWon || oWon || tied, board.isGameOver)
}

/**
 * Assert that an AI plays on the board with the given piece in the expected location.
 * @param board the board that is being played on, to be converted with createBoardFromString()
 * @param ai the AI making the play
 * @param piece the piece being played
 * @param r the expected row the piece will be played
 * @param c the expected column the piece will be played
 */
fun assertAIPlay(board: String, ai: AIPlayer, piece: Char, r: Int, c: Int) =
    assertAIPlay(Board.createFromString(board), ai, piece, r, c)

/**
 * Assert that an AI plays on the board with the given piece in the expected location.
 * @param board the board that is being played on
 * @param ai the AI making the play
 * @param piece the piece being played
 * @param r the expected row the piece will be played
 * @param c the expected column the piece will be played
 */
fun assertAIPlay(board: Board, ai: AIPlayer, piece: Char, r: Int, c: Int) {
    var x = board.countPieces('X')
    var o = board.countPieces('O')
    ai.play(board, piece)
    if (piece == 'X') x++ else o++
    assertPieceCounts(board, x, o)
    assertEquals(piece, board[r, c])
}

/**
 * Assert that the AI plays randomly on the given board with the given piece. The board will be
 * copied many times and it will be checked that the AI will end up playing on any available
 * space on the original board.
 * @param board the board that is being played on, to be converted with createBoardFromString()
 * @param ai the AI making the play
 * @param piece the piece being played
 * @return one of the board with the piece randomly played on it
 */
fun assertAIPlaysRandomly(board: String, ai: AIPlayer, piece: Char): Board =
    assertAIPlaysRandomly(Board.createFromString(board), ai, piece)

/**
 * Assert that the AI plays randomly on the given board with the given piece. The board will be
 * copied many times and it will be checked that the AI will end up playing on any available
 * space on the original board.
 * @param board the board that is being played on
 * @param ai the AI making the play
 * @param piece the piece being played
 * @return one of the boards with the piece randomly played on it
 */
fun assertAIPlaysRandomly(board: Board, ai: AIPlayer, piece: Char): Board {
    var x = board.countPieces('X')
    var o = board.countPieces('O')
    val spaces = 9 - x - o
    if (piece == 'X') x++ else o++
    val boards = HashSet<Board>()
    for (i in 0..50) {
        val b = board.copy()
        ai.play(b, piece)
        assertPieceCounts(b, x, o)
        boards.add(b)
    }
    assertEquals(spaces, boards.size)
    return boards.first()
}

/**
 * Checks a predicate against all coordinates. The predicate is called with each combination of
 * row and column on a 3x3 grid.
 */
fun assertAllCoords(predicate: (r: Int, c: Int) -> Boolean) {
    assertTrue((0..8).all { predicate(it / 3, it % 3) })
}
