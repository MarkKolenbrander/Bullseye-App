package com.yourcompany.bullseye

import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import bullseye.R
import bullseye.databinding.ActivityMainBinding
import kotlin.math.abs

import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private var sliderValue = 0
    private var targetValue = newTargetValue()
    private var totalScore = 0
    private var gameRound = 1

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        startNewGame()

        binding.buttonHitMe.setOnClickListener {
            showResult()
            totalScore += pointsForCurrentRound()
            binding.gameScoreTextView?.text = totalScore.toString()
        }

        binding.startOverButton?.setOnClickListener {
            startNewGame()
        }

        binding.infoButton?.setOnClickListener {
            navigateToAboutPage()
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                sliderValue = progress
            }
            override fun onStartTrackingTouch(seekbar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekbar: SeekBar?) {
            }
        })
    }

    private fun navigateToAboutPage() {
        val intent = Intent(this, AboutActivity::class.java)
        startActivity(intent)
    }

    private fun differenceAmount() = abs(targetValue - sliderValue)
    private fun newTargetValue() = Random.nextInt(0, 100)

    private fun pointsForCurrentRound(): Int {
        val maxScore = 100
        val difference = differenceAmount()
        var bonus = 0

        if (difference == 0) {
            bonus = 100
        } else if (difference == 1) {
            bonus = 50
        }
        return maxScore - difference + bonus
    }

    private fun startNewGame(){
        totalScore = 0
        gameRound = 1
        sliderValue = 50
        targetValue = newTargetValue()

        binding.targetTextView.text = targetValue.toString()
        binding.gameRoundSTextView?.text = gameRound.toString()
        binding.gameScoreTextView?.text = totalScore.toString()
        binding.seekBar.progress = sliderValue
    }

    private fun showResult() {
        val dialogTitle = alertTitle()
        val dialogMessage =
            getString(R.string.result_dialog_message, sliderValue, pointsForCurrentRound())
        val builder = AlertDialog.Builder(this)

        builder.setTitle(dialogTitle)
        builder.setMessage(dialogMessage)
        builder.setPositiveButton(R.string.result_dialog_button_text) { dialog, _ ->
            targetValue = newTargetValue()
            binding.targetTextView.text = targetValue.toString()
            gameRound ++
            binding.gameRoundSTextView?.text = gameRound.toString()
            dialog.dismiss()
        }
        builder.create().show()
    }

    private fun alertTitle(): String {
        val difference = differenceAmount()

        val title: String = when {
            difference == 0 -> {
                getString(R.string.alert_title_1)
            }
            difference < 5 -> {
                getString(R.string.alert_title_2)
            }
            difference <=10 -> {
                getString(R.string.alert_title_3)
            }
            else -> {
                getString(R.string.alert_title_4)
            }
        }
        return title
    }


}