package com.example.dinogame

import android.os.Bundle
import android.os.Vibrator
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import java.util.Timer
import android.os.Build
import android.os.VibratorManager
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class GameActivity : AppCompatActivity(){
    private lateinit var gameView : GameView
    private lateinit var dinoGame : DinoGame
    private lateinit var detector : GestureDetector //remove

    private lateinit var vibrator: Vibrator
    private lateinit var gameTimer : Timer
    private lateinit var ad : InterstitialAd
    private lateinit var score: DatabaseReference


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
        gameTimer = Timer()
        gameTimer.schedule(task, 0L, 50)

        val vibratorManager = getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibrator = vibratorManager.defaultVibrator

        var firebase : FirebaseDatabase = FirebaseDatabase.getInstance()
        score = firebase.getReference("score")

    }

    fun getDinoGame() : DinoGame {
        return dinoGame
    }

    fun vibrate() {
        Log.w("GameActivity", "should be vibrating")
        vibrator.vibrate(1000)
    }

    fun updateModel() {
        dinoGame.moveCactus()
        dinoJump()

        if (dinoGame.cactusOffScreen()) {
            dinoGame.resetCactus()
        } else if (dinoGame.dinoHit()) {
            vibrate()
            dinoGame.setDinoHit(true)
            gameTimer.cancel()
            var builder : AdRequest.Builder = AdRequest.Builder( )
            builder.addKeyword( "gaming" )
            var request : AdRequest = builder.build()

            var adUnitId : String = "ca-app-pub-3940256099942544/1033173712"
            var adLoadHandler : AdLoadHandler = AdLoadHandler( )
            runOnUiThread { InterstitialAd.load( this, adUnitId, request, adLoadHandler ) }

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

    inner class AdLoadHandler : InterstitialAdLoadCallback() {
        override fun onAdFailedToLoad(p0: LoadAdError) {
            super.onAdFailedToLoad(p0)
        }

        override fun onAdLoaded(p0: InterstitialAd) {
            super.onAdLoaded(p0)
            ad = p0
            ad.show( this@GameActivity )
            // manage the ad
            var adMgmt : AdManagement = AdManagement()
            ad.fullScreenContentCallback = adMgmt
        }
    }

    inner class AdManagement : FullScreenContentCallback() {
        override fun onAdClicked() {
            super.onAdClicked()
        }

        override fun onAdDismissedFullScreenContent() {
            super.onAdDismissedFullScreenContent()

            finish()
        }

        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
            super.onAdFailedToShowFullScreenContent(p0)
        }

        override fun onAdImpression() {
            super.onAdImpression()
        }

        override fun onAdShowedFullScreenContent() {
            super.onAdShowedFullScreenContent()
        }
    }
}