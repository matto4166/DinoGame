package com.example.dinogame

import android.media.SoundPool
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
    private var dinoGame : DinoGame = MainActivity.dinoGame
    private lateinit var detector : GestureDetector

    private lateinit var vibrator: Vibrator
    private lateinit var gameTimer : Timer
    private lateinit var pool : SoundPool
    private var jumpSound : Int = 0
    private lateinit var ad : InterstitialAd

    private lateinit var scores: DatabaseReference
    private var adCalled : Boolean = false




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var width : Int = resources.displayMetrics.widthPixels
        var height : Int = resources.displayMetrics.heightPixels
        gameView = GameView(this, width, height, dinoGame)

        var th = TouchHandler()
        detector = GestureDetector(this, th)

        setContentView(gameView)

        var builder : SoundPool.Builder = SoundPool.Builder()
        pool = builder.build()
        jumpSound = pool.load(this, R.raw.jump, 1)

        var task : GameTimerTask = GameTimerTask(this@GameActivity)
        gameTimer = Timer()
        gameTimer.schedule(task, 0L, 50)

        val vibratorManager = getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibrator = vibratorManager.defaultVibrator

        var firebase : FirebaseDatabase = FirebaseDatabase.getInstance()
        scores = firebase.getReference("scores")

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
            gameTimer.cancel()

            if (dinoGame.getScore() > dinoGame.getHighScore()) {
                val previousHigh = dinoGame.getHighScore()
                val newHigh = dinoGame.getScore()
                scores.get().addOnSuccessListener { snapshot ->
                    // Remove previous high score if it exists
                    for (child in snapshot.children) {
                        val value = child.getValue(Int::class.java) ?: 0
                        if (value == previousHigh) {
                            child.ref.removeValue()
                            break
                        }
                    }

                    scores.push().setValue(newHigh)

                    dinoGame.setHighScore(newHigh)
                    dinoGame.setPreferences(this)
                }.addOnFailureListener { e ->
                    Log.e("GameActivity", "Failed to read Firebase: ${e.message}")
                }
            }


            dinoGame.setScore(0)
            dinoGame.setPreferences(this)

            vibrate()
            dinoGame.setDinoHit(true)
            adCall(adCalled)
        }
    }

    fun adCall(called : Boolean) {
        if (called == false) {
            adCalled = true
            var builder : AdRequest.Builder = AdRequest.Builder( )
            builder.addKeyword( "gaming" )
            var request : AdRequest = builder.build()

            var adUnitId : String = "ca-app-pub-3940256099942544/1033173712"
            var adLoadHandler : AdLoadHandler = AdLoadHandler( )
            runOnUiThread { InterstitialAd.load( this@GameActivity, adUnitId, request, adLoadHandler ) }
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
                dinoGame.setScore(dinoGame.getScore() + 1)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        detector.onTouchEvent(event)
        return true
    }

    inner class TouchHandler : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            if (!dinoGame.dinoHit()) {
                dinoGame.jump()
                pool.play(jumpSound, 1f, 1f,1, 0, 1f)
            }
            return super.onDown(e)
        }
    }

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