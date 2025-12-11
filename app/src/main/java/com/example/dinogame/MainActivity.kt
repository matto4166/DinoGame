package com.example.dinogame

import android.content.Context
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
    private lateinit var highScoreText : TextView
    private lateinit var scoresButton : Button
    private lateinit var customizeButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        dinoGame = DinoGame(this)

        setContentView(R.layout.activity_main)

        val sp = getSharedPreferences(packageName + "_preferences", Context.MODE_PRIVATE)
        val localHighScore = sp.getInt("HIGH_SCORE", 0)
        characterNum = sp.getInt("CHARACTER", 0)
        highScore = localHighScore

        highScoreText = findViewById<TextView>(R.id.highscore_tv)
        highScoreText.text = "Your High Score: " + highScore

        playButton = findViewById<Button>(R.id.play_button)
        playButton.setOnClickListener{play()}

        scoresButton = findViewById<Button>(R.id.scores)
        scoresButton.setOnClickListener { leaderboard() }

        customizeButton = findViewById<Button>(R.id.customize)
        customizeButton.setOnClickListener { customize() }
    }

    override fun onResume() {
        super.onResume()
//        dinoGame.setDinoHit(false)
        val sp = getSharedPreferences(packageName + "_preferences", Context.MODE_PRIVATE)
        val localHighScore = sp.getInt("HIGH_SCORE", 0)
        highScore = localHighScore
        highScoreText.text = "Your High Score: " + highScore
    }

    fun play() {
        var intent : Intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
    }

    fun leaderboard() {
        var intent : Intent = Intent(this, Leaderboard::class.java)
        startActivity(intent)
    }

    fun customize() {
        var intent : Intent = Intent(this, Customize::class.java)
        startActivity(intent)
    }

    companion object {
//        lateinit var dinoGame : DinoGame
        var highScore : Int = 0
        var characterNum : Int = 0
    }

}