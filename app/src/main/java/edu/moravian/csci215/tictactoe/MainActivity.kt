/**
 * TEAM MEMBERS: Tyler Valentine, David Olsakowski, Reed Sturza
 */

package edu.moravian.csci215.tictactoe

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    /**
     * The list of all of the tic-tac-toe button resource IDs, in order from 0 to 8 (the same as their
     * indices).
     */
    private val buttonIds = listOf(
        R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4,
        R.id.button5, R.id.button6, R.id.button7, R.id.button8)

    /**
     * The map of all board locations on the tic-tac-toe board. This is
     * used to convert the button numbers to the board location in the Board class.
     */
    private val boardLocations = mapOf(0 to Pair(0, 0), 1 to Pair(0, 1), 2 to Pair(0, 2),
        3 to Pair(1, 0), 4 to Pair(1, 1), 5 to Pair(1, 2), 6 to Pair(2, 0),
        7 to Pair(2, 1), 8 to Pair(2, 2))

    /** Variables for two players. */
    private lateinit var player1: Player
    private lateinit var player2: Player

    /** Variable for tic-tac-toe game. */
    private val game = Game()

    /** Launcher to restart the game when play again button is clicked on game over screen. */
    private val gameLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        game.startNewRound()
        updateBoard()
    }

    /** View for the display text at the bottom of the application. */
    private lateinit var displayView: TextView

    /** Variable to store result of current game. */
    private lateinit var gameResult: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        makePlayers()
        game.startGame(player1, player2)
        game.startNewRound()

        game.onRoundOverListener = {
            result -> gameResult = when (result) {
            Game.Result.TIE -> getString(R.string.tie_text)
            Game.Result.PLAYER_1_WIN -> getString(R.string.winning_toast, game.players[0].name)
            Game.Result.PLAYER_2_WIN -> getString(R.string.winning_toast, game.players[1].name)
        }
            changeScreenView(view = null)
        }

        displayView = findViewById(R.id.display_text_view)

        for(index in buttonIds.indices)
        {
            findViewById<Button>(buttonIds[index]).setOnClickListener {
                playTurn(index)
            }
        }
        updateBoard()
    }

    /** Updates every board piece based on current state of the game. */
    private fun updateBoard()
    {
        for(index in buttonIds.indices) {
            val button = findViewById<Button>(buttonIds[index])
            button.text = game.board[index/3, index%3].toString()
        }
        if (game.turn != -1) {
            displayView.text = getString(
                R.string.display_text, game.players[game.turn].name,
                game.pieces[game.turn]
            )
        }
    }

    /**
     * PLay turn based on position of the board given.
     * @param index location of board to play piece.
     */
    private fun playTurn(index: Int) {
        if (!game.roundInProgress) {
            showErrorMessage()
        } else {
            val playResult =
                game.playPiece(boardLocations[index]!!.first, boardLocations[index]!!.second)
            if (!playResult)
                showErrorMessage()
        }
        updateBoard()
    }

    /** Make player types based on data received from welcome screen. */
    private fun makePlayers() {
       player1 = setPlayer(intent.getStringExtra("player_1_name") ?: "",
            intent.getIntExtra("player_1_type", 0))

        player2 = setPlayer(intent.getStringExtra("player_2_name") ?: "",
                intent.getIntExtra("player_2_type", 0))
    }

    /**
     * Sets player types and names based on data given.
     * @param name name of player
     * @param type type of player
     */
    private fun setPlayer(name: String, type: Int): Player {
        return when (type) {
            0 -> HumanPlayer(name)
            1 -> EasyAIPlayer()
            2 -> MediumAIPlayer()
            else -> HardAIPlayer()
        }
    }


    /**
     * Show toast when a player has attempted to put a piece in a spot that already contains one,
     * or a player has tried putting a piece on the board after the game has concluded.
     */
    private fun showErrorMessage()
    {
        Toast.makeText(this, resources.getString(R.string.error_toast), Toast.LENGTH_SHORT).show()
    }

    /** Function to change screen view to game over when game ends. */
    fun changeScreenView(view: View?) {
        val intent = Intent(this, GameOverActivity::class.java)

        intent.putExtra("game_result", gameResult)
        intent.putExtra("tie_total", game.ties.toString())
        intent.putExtra("player_1_win_total", game.wins1.toString())
        intent.putExtra("player_2_win_total", game.wins2.toString())
        gameLauncher.launch(intent)
    }
}
