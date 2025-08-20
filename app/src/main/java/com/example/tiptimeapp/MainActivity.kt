package com.example.tiptimeapp

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tiptimeapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var totalTip = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        totalTip = savedInstanceState?.getDouble("tip") ?: 0.0
        binding.tipAmountLabel.text = totalTip.toString()
        /*binding.tipAmountLabel.text = totalTip.toString()
        binding.costOfServiceEditText.setText(savedInstanceState?.getString("cost"))
        binding.roundUpSwitch.isChecked = savedInstanceState?.getBoolean("roundUp") ?: false*/
        
        binding.calculateTipButton.setOnClickListener { calculateTip() }
        
    }
    
    private fun calculateTip() {
        val stringInTextField = binding.costOfServiceEditText.text.toString()
        val cost = stringInTextField.toDoubleOrNull()
        if (cost == null) {
            binding.tipAmountLabel.text = ""
            return
        }
        val selectedId = binding.tipOptions.checkedRadioButtonId
        val tipPercentage = when (selectedId) {
            R.id.amazing -> 0.20
            R.id.okay -> 0.18
            else -> 0.15
        }
        var tip = tipPercentage * cost
        if (binding.roundUpSwitch.isChecked) {
            tip = kotlin.math.ceil(tip)
        }
        val totalTip = "%.2f".format(tip)
        binding.tipAmountLabel.text = buildString {
        append("$ ")
        append(totalTip)
    }
        
        Snackbar.make(binding.calculateTipButton, "Tip Amount: $totalTip", Snackbar.LENGTH_LONG).show()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString("tip", binding.tipAmountLabel.text.toString())
        /*outState.putString("cost", binding.costOfServiceEditText.text.toString())
        outState.putBoolean("roundUp", binding.roundUpSwitch.isChecked)*/
    }
    
}