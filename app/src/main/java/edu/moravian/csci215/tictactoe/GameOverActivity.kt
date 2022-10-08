/**
 * TEAM MEMBERS: Tyler Valentine, David Olsakowski, Reed Sturza
 */

package edu.moravian.csci215.tictactoe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.moravian.csci215.tictactoe.databinding.ActivityGameoverBinding

class GameOverActivity: AppCompatActivity() {

    private lateinit var binding: ActivityGameoverBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameoverBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setDisplayTexts()
        binding.playAgain.setOnClickListener { finish() }
    }

    /** Sets display texts on screen based on incoming data about the game results. */
    private fun setDisplayTexts()
    {
        binding.result.text = intent.getStringExtra("game_result")
        binding.ties.text = resources.getString(R.string.tie_total_text, intent.getStringExtra("tie_total"))
        binding.player1Wins.text = resources.getString(R.string.player_one_win_total_text, intent.getStringExtra("player_1_win_total"))
        binding.player2Wins.text = resources.getString(R.string.player_two_win_total_text, intent.getStringExtra("player_2_win_total"))
    }

}