package com.example.stopwatch

import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var timerTextView: TextView
    private lateinit var startButton: Button
    private lateinit var pauseButton: Button
    private lateinit var resetButton: Button

    private var isRunning = false
    private var startTime: Long = 0
    private var elapsedTime: Long = 0
    private var timerThread: Thread? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timerTextView = findViewById(R.id.timer)
        startButton = findViewById(R.id.start_button)
        pauseButton = findViewById(R.id.pause_button)
        resetButton = findViewById(R.id.reset_button)

        startButton.setOnClickListener {
            startTimer()
        }

        pauseButton.setOnClickListener {
            pauseTimer()
        }

        resetButton.setOnClickListener {
            resetTimer()
        }
    }

    private fun startTimer() {
        if (!isRunning) {
            startTime = SystemClock.elapsedRealtime() - elapsedTime
            timerThread = Thread {
                isRunning = true
                while (isRunning) {
                    elapsedTime = SystemClock.elapsedRealtime() - startTime
                    runOnUiThread {
                        val milliseconds = (elapsedTime % 1000) / 10
                        val seconds = (elapsedTime / 1000) % 60
                        val minutes = (elapsedTime / 60000) % 60
                        timerTextView.text = String.format("%02d:%02d:%02d", minutes, seconds, milliseconds)
                    }
                    Thread.sleep(10)
                }
            }
            timerThread?.start()
        }
    }

    private fun pauseTimer() {
        isRunning = false
    }

    private fun resetTimer() {
        isRunning = false
        elapsedTime = 0
        timerTextView.text = "00:00:00"
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        timerThread?.interrupt()
    }
}
