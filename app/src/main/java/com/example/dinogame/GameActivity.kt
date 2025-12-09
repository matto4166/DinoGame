package com.example.dinogame

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import java.util.Timer

class GameActivity : AppCompatActivity(){
    private lateinit var gameView : GameView
    private lateinit var dinoGame : DinoGame
    private lateinit var detector : GestureDetector //remove


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var width : Int = resources.displayMetrics.widthPixels
        var height : Int = resources.displayMetrics.heightPixels
        gameView = GameView(this, width, height)
        dinoGame = MainActivity.game

            var th = TouchHandler()
            detector = GestureDetector(this, th)

        setContentView(gameView)

        var task : GameTimerTask = GameTimerTask(this@GameActivity)
        var gameTimer : Timer = Timer()
        gameTimer.schedule(task, 0L, 50)
    }

    fun getDinoGame() : DinoGame {
        return dinoGame
    }

    fun updateModel() {
        dinoGame.moveCactus()
        dinoJump()

        if (dinoGame.cactusOffScreen()) {
            dinoGame.resetCactus()
        } else if (dinoGame.dinoHit()) {
            dinoGame.setDinoHit(true)
            // Start the post game screen
        }
    }

    fun updateView() {
        gameView.postInvalidate()
    }

    fun dinoJump() {
        if (dinoGame.getJumping()) {
            val deltaTime = dinoGame.getDeltaTime() / 1000f

            dinoGame.getDinoRect()!!.top += (dinoGame.getJumpVelocity() * deltaTime).toInt()
            dinoGame.getDinoRect()!!.bottom += (dinoGame.getJumpVelocity() * deltaTime).toInt()

            dinoGame.setJumpVelocity(dinoGame.getJumpVelocity() + (dinoGame.getGravity() * deltaTime))

            if (dinoGame.getDinoRect()!!.bottom >= dinoGame.getGround()) {
                var difference = dinoGame.getDinoRect()!!.bottom - dinoGame.getGround()
                dinoGame.getDinoRect()!!.top -= difference
                dinoGame.getDinoRect()!!.bottom = dinoGame.getGround()

                dinoGame.setJumping(false)
                dinoGame.setJumpVelocity(0f)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        detector.onTouchEvent(event)
        return true
    } // remove

    inner class TouchHandler : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            dinoGame.jump()
            return super.onDown(e)
        }
    } // remove
}