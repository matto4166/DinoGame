package com.example.dinogame

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var playButton : Button
    private lateinit var highScore : TextView
    private lateinit var scoresButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dinoGame = DinoGame(this)

        setContentView(R.layout.activity_main)

        highScore = findViewById<TextView>(R.id.highscore_tv)
        highScore.text = "Your High Score: " + dinoGame.getHighScore().toString()

        playButton = findViewById<Button>(R.id.play_button)
        playButton.setOnClickListener{play()}

        scoresButton = findViewById<Button>(R.id.scores)
        scoresButton.setOnClickListener { leaderboard() }
    }

    override fun onResume() {
        super.onResume()
        dinoGame.setDinoHit(false)
        highScore.text = "Your High Score: " + dinoGame.getHighScore().toString()
    }

    fun play() {
        var intent : Intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
    }

    fun leaderboard() {
        var intent : Intent = Intent(this, Leaderboard::class.java)
        startActivity(intent)
    }

    companion object {
        lateinit var dinoGame : DinoGame
    }

}