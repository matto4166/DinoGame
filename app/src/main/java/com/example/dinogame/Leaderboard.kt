package com.example.dinogame

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Leaderboard : AppCompatActivity() {
    private lateinit var backButton: Button
    private lateinit var scores: DatabaseReference
    private val scoreViews = mutableListOf<TextView>()
    private lateinit var scoreText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.leaderboard)

        backButton = findViewById(R.id.backButton)
        backButton.setOnClickListener { finish() }

        scoreText = findViewById(R.id.scoreText)

        val ids = listOf(
            R.id.highestScore,
            R.id.highestScore2,
            R.id.highestScore3,
            R.id.highestScore4,
            R.id.highestScore5,
            R.id.highestScore6
        )

        for (id in ids) {
            val tv = findViewById<TextView>(id)
            if (tv != null) {
                scoreViews.add(tv)
            } else {
                Log.e("Leaderboard", "TextView with id $id not found in layout!")
            }
        }

        scores = FirebaseDatabase.getInstance().getReference("scores")

        scores.addValueEventListener(ScoresListener())
    }
    inner class ScoresListener : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val scoresList = mutableListOf<Int>()

            for (child in snapshot.children) {
                val score = child.getValue(Int::class.java) ?: 0
                scoresList.add(score)
            }

            scoresList.sortDescending()
            val topFirebaseScore = scoresList[0]
            for (i in scoreViews.indices) {
                val placement = scoreViews[i]
                if (i < scoresList.size) {
                    placement.text = "Score ${i + 1}: ${scoresList[i]}"
                } else {
                    placement.text = "Score ${i + 1}: "
                }
            }

            val sp = getSharedPreferences(packageName + "_preferences", Context.MODE_PRIVATE)
            val localHighScore = sp.getInt("HIGH_SCORE", 0)

            scoreText.text =
                if (localHighScore >= topFirebaseScore) {
                    "You have the highest score!"
                } else {
                    val percentage = (localHighScore.toDouble() / topFirebaseScore.toDouble()) * 100
                    "You are %.1f%% of the top score.".format(percentage)
            }

        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("Leaderboard", "error: " + error.message)
        }
    }
}
