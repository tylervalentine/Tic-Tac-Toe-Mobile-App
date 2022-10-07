package edu.moravian.csci215.tictactoe

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import edu.moravian.csci215.tictactoe.databinding.ActivityWelcomeBinding

class WelcomeActivity: AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private var spinnerOptions = listOf("Human", "Easy", "Medium", "Hard")

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSpinnerOptions(spinnerOptions)
        binding.spinner.onItemSelectedListener = this
        binding.spinner2.onItemSelectedListener = this

    }

    fun changeScreenView(view: View?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun setSpinnerOptions(optionsList: List<String>) {
        val dataAdapter = ArrayAdapter(this, R.layout.spinner_style, optionsList)
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_style)
        binding.spinner.adapter = dataAdapter
        binding.spinner2.adapter = dataAdapter
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        if(parent?.id == R.id.spinner) {
            if(parent.getItemAtPosition(pos).toString() == "Human") {
                binding.name1.visibility = View.VISIBLE
                binding.nameText1.visibility = View.VISIBLE
            }
            else
            {
                binding.name1.visibility = View.GONE
                binding.nameText1.visibility = View.GONE
            }
        }

        if(parent?.id == R.id.spinner2) {
            if(parent.getItemAtPosition(pos).toString() == "Human") {
                binding.name2.visibility = View.VISIBLE
                binding.nameText2.visibility = View.VISIBLE
            }
            else
            {
                binding.name2.visibility = View.GONE
                binding.nameText2.visibility = View.GONE
            }
        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) { }
}