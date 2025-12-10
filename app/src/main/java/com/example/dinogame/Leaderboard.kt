package com.example.dinogame

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Leaderboard: AppCompatActivity() {
    private lateinit var backButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.leaderboard)

        backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener { back() }
    }

    fun back() {
        finish()
    }

}