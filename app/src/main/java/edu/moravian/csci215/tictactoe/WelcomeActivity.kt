/**
 * TEAM MEMBERS: Tyler Valentine, David Olsakowski, Reed Sturza
 */

package edu.moravian.csci215.tictactoe

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import edu.moravian.csci215.tictactoe.databinding.ActivityWelcomeBinding

class WelcomeActivity: AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.spinner.onItemSelectedListener = this
        binding.spinner2.onItemSelectedListener = this

    }

    /** Function to change screen view to game when game ends, sends data about players. */
    fun changeScreenView(view: View?) {
        val intent = Intent(this, MainActivity::class.java)

        intent.putExtra("player_1_name", binding.nameText1.text.toString())
        intent.putExtra("player_2_name", binding.nameText2.text.toString())
        intent.putExtra("player_1_type", binding.spinner.selectedItemPosition)
        intent.putExtra("player_2_type", binding.spinner2.selectedItemPosition)
        startActivity(intent)
    }

    /**
     * Makes name text box visible or invisible based on what the user has selected from
     * the spinners.
     * */
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        if(parent?.id == R.id.spinner) {
            if(parent.getItemAtPosition(pos).toString() == "Human") {
                binding.nameText1.visibility = View.VISIBLE
            }
            else
            {
                binding.nameText1.visibility = View.INVISIBLE
            }
        }

        if(parent?.id == R.id.spinner2) {
            if(parent.getItemAtPosition(pos).toString() == "Human") {
                binding.nameText2.visibility = View.VISIBLE
            }
            else
            {
                binding.nameText2.visibility = View.INVISIBLE
            }
        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) { }
}