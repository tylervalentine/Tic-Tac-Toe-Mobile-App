package edu.moravian.csci215.tictactoe

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import edu.moravian.csci215.tictactoe.databinding.ActivityGameoverBinding

class GameOverActivity : AppCompatActivity() {
    //private lateinit var displayView2: TextView
    private lateinit var binding: ActivityGameoverBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameoverBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
    fun changeScreenView(view: View?) {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
    }
}