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

    private lateinit var refScore: DatabaseReference
    private lateinit var scoresButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dinoGame = DinoGame(this)

        setContentView(R.layout.activity_main)

        highScore = findViewById<TextView>(R.id.highscore_tv)
        highScore.text = "Your High Score: " + dinoGame.getHighScore().toString()

//        refScore = FirebaseDatabase.getInstance().getReference("scores")
//        refScore.addValueEventListener(ScoresListener())

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

//    inner class ScoresListener : ValueEventListener {
//        override fun onDataChange(snapshot: DataSnapshot) {
//            val scoresList = mutableListOf<Int>()
//
//            for (child in snapshot.children) {
//                val score: Int = child.getValue(Int::class.java) as Int
//                scoresList.add(score)
//            }
//
//            if (scoresList.isNotEmpty()) {
//                val maxScore = scoresList.maxOrNull() ?: 0
//                highScore.text = "High Score: $maxScore"
//                game.setHighScore(maxScore)
//            } else {
//                highScore.text = "High Score: 0"
//            }
//
//            Log.w("MainActivity", "Updated scores: $scoresList")
//        }
//
//        override fun onCancelled(error: DatabaseError) {
//            Log.w( "MainActivity", "error: " + error.message )
//        }
//    }

    companion object {
        lateinit var dinoGame : DinoGame
    }

}