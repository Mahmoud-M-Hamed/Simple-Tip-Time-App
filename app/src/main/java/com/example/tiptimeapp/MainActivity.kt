package com.example.tiptimeapp

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.example.tiptimeapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var totalTip: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(binding.root)


        setupInsets()


        totalTip = savedInstanceState?.getString("tip") ?: "Tip Amount"
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
            "Tip Amount".also { binding.tipAmountLabel.text = it }
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

        Snackbar.make(binding.calculateTipButton, "Tip Amount: $totalTip", Snackbar.LENGTH_LONG)
            .setAction("Proceed", {
                binding.costOfServiceEditText.text?.clear()
                "Tip Amount".also { binding.tipAmountLabel.text = it }
                binding.roundUpSwitch.isChecked = true
                binding.amazing.isChecked = true
                binding.tipOptions.check(R.id.amazing)

            }).show()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString("tip", binding.tipAmountLabel.text.toString())
        /*outState.putString("cost", binding.costOfServiceEditText.text.toString())
        outState.putBoolean("roundUp", binding.roundUpSwitch.isChecked)*/
    }


    private fun setupInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.statusBars())
            v.updatePadding(insets.left, insets.top, insets.right, insets.bottom)
            WindowCompat.getInsetsController(window, window.decorView).apply {
                isAppearanceLightStatusBars = true
            }

            val systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            WindowInsetsCompat.CONSUMED

        }

    }

}