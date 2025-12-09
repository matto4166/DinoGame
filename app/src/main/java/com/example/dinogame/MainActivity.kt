package com.example.dinogame

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var playButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        playButton = findViewById<Button>(R.id.play_button)
        playButton.setOnClickListener{play()}
    }

    fun play() {
        var intent : Intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
    }
}