package edu.moravian.csci215.tictactoe

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    /**
     * The list of all of the tic-tac-toe button resource IDs, in order from 0 to 8 (the same as their
     * indices).
     */
    private val buttonIds = listOf(
        R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4,
        R.id.button5, R.id.button6, R.id.button7, R.id.button8)

    /** The list of both tic-tac-toe button symbols. */
    private val pieceTypes = listOf("X", "O")

    /**
     * The map of all board locations on the  tic-tac-toe board. This is
     * used to convert the button numbers to the board location in the Board class.
     */
    private val boardLocations = mapOf(0 to Pair(0, 1), 1 to Pair(0, 2), 2 to Pair(0, 3),
        3 to Pair(1, 1), 4 to Pair(2, 2), 5 to Pair(2, 3), 6 to Pair(3, 1),
        7 to Pair(3, 2), 8 to Pair(3, 3))

    /** Variables for two test human players. */
    private val player1 = HumanPlayer("Test")
    private val player2 = HumanPlayer("Other Test")

    /** Variable for tic-tac-toe game. */
    private val game = Game()

    private lateinit var displayView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        game.startGame(player1, player2)
        game.startNewRound()
        displayView = findViewById(R.id.display_text_view)
        displayView.text = resources.getString(R.string.display_text, game.players[game.turn].name,
            game.pieces[game.turn])

        for(index in buttonIds.indices)
        {
            findViewById<Button>(buttonIds[index]).setOnClickListener {
                setButtonView(index)
            }
        }
    }

    /**
     * Set the button text view specified in num.
     * @param num number of button to update on layout.
     */
    private fun setButtonView(num: Int)
    {
        findViewById<Button>(buttonIds[num]).text = game.pieces[game.turn].toString()
        game.playPiece(boardLocations[num]!!.first, boardLocations[num]!!.second)
        displayView.text = resources.getString(R.string.display_text, game.players[game.turn].name, game.pieces[game.turn])
    }
}
