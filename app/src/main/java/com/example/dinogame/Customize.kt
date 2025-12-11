package com.example.dinogame

import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity

class Customize : AppCompatActivity() {
    private lateinit var button : Button
    private lateinit var cuteDino : RadioButton
    private lateinit var longNeckDino : RadioButton
    private lateinit var dinoRider : RadioButton
    private lateinit var chicken : RadioButton
    private lateinit var mario : RadioButton
    private var dinoGame : DinoGame = MainActivity.dinoGame


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.customize)

        cuteDino = findViewById<RadioButton>(R.id.cute_dino)
        longNeckDino = findViewById<RadioButton>(R.id.long_neck_dino)
        dinoRider = findViewById<RadioButton>(R.id.dino_rider)
        chicken = findViewById<RadioButton>(R.id.chicken)
        mario = findViewById<RadioButton>(R.id.mario)

        button = findViewById<Button>(R.id.backButton)
        button.setOnClickListener { goBack() }

        updateView()
    }

    fun goBack() {
        updateDinoObject()
        dinoGame.setDinoPreferences(this)
        finish()
    }

    fun updateView() {
        var characterNum = dinoGame.getCharacterNum()

        if (characterNum == 0) {
            cuteDino.isChecked = true
        } else if (characterNum == 1) {
            longNeckDino.isChecked = true
        } else if (characterNum == 2) {
            dinoRider.isChecked = true
        } else if (characterNum == 3) {
            chicken.isChecked = true
        } else if (characterNum == 4) {
            mario.isChecked = true
        }
    }

    fun updateDinoObject() {
        if (cuteDino.isChecked) {
            dinoGame.setCharacterNum(0)
        } else if (longNeckDino.isChecked) {
            dinoGame.setCharacterNum(1)
        } else if (dinoRider.isChecked) {
            dinoGame.setCharacterNum(2)
        } else if (chicken.isChecked) {
            dinoGame.setCharacterNum(3)
        } else if (mario.isChecked) {
            dinoGame.setCharacterNum(4)
        }
    }

}