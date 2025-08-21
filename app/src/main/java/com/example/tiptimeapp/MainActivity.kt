package com.example.tiptimeapp

import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.example.tiptimeapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlin.math.ceil

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
        restoreState(savedInstanceState)
        setupListeners()
    }

    // -------------------------------
    // UI Setup
    // -------------------------------
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

    private fun setupListeners() {
        binding.calculateTipButton.setOnClickListener { calculateTip() }
    }

    // -------------------------------
    // State Handling
    // -------------------------------
    private fun restoreState(savedInstanceState: Bundle?) {
        totalTip = savedInstanceState?.getString("tip") ?: "Tip Amount"
        binding.tipAmountLabel.text = totalTip
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString("tip", binding.tipAmountLabel.text.toString())
    }

    // -------------------------------
    // Tip Calculation
    // -------------------------------
    private fun calculateTip() {
        val cost = binding.costOfServiceEditText.text.toString().toDoubleOrNull()

        if (cost == null) {
            showInvalidInputSnackBar()
            "Tip Amount".also { binding.tipAmountLabel.text = it }
            return
        }

        val tipPercentage = when (binding.tipOptions.checkedRadioButtonId) {
            R.id.amazing -> 0.20
            R.id.okay -> 0.18
            else -> 0.15
        }

        var tip = tipPercentage * cost
        if (binding.roundUpSwitch.isChecked) tip = ceil(tip)

        val formattedTip = "%.2f".format(tip)
        "$ $formattedTip".also { binding.tipAmountLabel.text = it }

        showResetSnackBar()
    }

    // -------------------------------
    // SnackBar Helpers
    // -------------------------------
    private fun showInvalidInputSnackBar() =
        Snackbar.make(
            binding.calculateTipButton,
            "Please enter a valid cost of service amount",
            Snackbar.LENGTH_LONG
        ).setBackgroundTint(Color.RED).show()


    private fun showResetSnackBar() {
        val snackBar =
            Snackbar.make(binding.calculateTipButton, "Reset Service", Snackbar.LENGTH_LONG)

        // Underlined action text
        val actionText = SpannableString("Proceed").apply {
            setSpan(UnderlineSpan(), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        snackBar.setAction(actionText) { resetForm() }
        snackBar.setActionTextColor(Color.CYAN)

        // Reduce font size
        val actionTextView = snackBar.view.findViewById<TextView>(
            com.google.android.material.R.id.snackbar_action
        )
        actionTextView?.textSize = 11f

        snackBar.show()
    }

    // -------------------------------
    // Reset Helpers
    // -------------------------------
    private fun resetForm() {
        binding.costOfServiceEditText.text?.clear()
        "Tip Amount".also { binding.tipAmountLabel.text = it }
        binding.roundUpSwitch.isChecked = true
        binding.amazing.isChecked = true
        binding.tipOptions.check(R.id.amazing)
    }
}
