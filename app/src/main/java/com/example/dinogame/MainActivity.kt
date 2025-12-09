package com.example.dinogame

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var playButton : Button
    private lateinit var highScore : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        game = DinoGame()

        highScore = findViewById<TextView>(R.id.highscore_tv)
        highScore.text = "High Score: " + game.getHighScore()

        playButton = findViewById<Button>(R.id.play_button)
        playButton.setOnClickListener{play()}
    }

    fun play() {
        var intent : Intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
    }

    companion object {
        lateinit var game : DinoGame
    }
}