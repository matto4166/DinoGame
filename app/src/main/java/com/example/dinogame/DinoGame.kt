package com.example.dinogame

import android.graphics.Rect

class DinoGame {
    private var screenRect : Rect? = null
    private var dinoRect : Rect? = null
    private var cactusRect : Rect? = null

    private var dinoWidth = 0
    private var dinoHeight = 0
    private var cactusWidth = 0
    private var cactusHeight = 0
    private var cactusSpeed : Float = 0f

    private var deltaTime : Float = 0f
    private var score : Int = 0
    private var highScore : Int = 0
    private var dinoHit : Boolean = false // Use this to judge if game is over

    private var jumping : Boolean = false
    private val gravity : Float = 900f
    private var jumpVelocity : Float = 0f
    private val jumpForce : Float =  -1000f
    private var ground = 0

    constructor(screenRect : Rect, dinoRect : Rect, cactusRect : Rect, cactusSpeed : Float, deltaTime : Float) {
        this.screenRect = screenRect
        setDinoRect(dinoRect)
        setCactusRect(cactusRect)
        this.cactusSpeed = cactusSpeed
        this.deltaTime = deltaTime
    }

    fun getDinoRect() : Rect {
        return dinoRect!!
    }

    fun getCactusRect() : Rect {
        return cactusRect!!
    }

    fun getDeltaTime() : Float {
        return deltaTime
    }

    fun getScore() : Int {
        return score
    }

    fun setScore(score : Int) : Unit {
        this.score = score
    }

    fun getHighScore() : Int {
        return highScore
    }

    fun setHighScore(highScore : Int) : Unit {
        this.highScore = highScore
    }

    fun getDinoHit() : Boolean {
        return dinoHit
    }

    fun getGravity() : Float {
        return gravity
    }
    fun getJumpVelocity() : Float {
        return jumpVelocity
    }

    fun setJumpVelocity(jumpVelocity : Float) {
        this.jumpVelocity = jumpVelocity
    }
    fun getJumping() : Boolean {
        return jumping
    }

    fun setJumping(jumping : Boolean) {
        this.jumping = jumping
    }

    fun getGround() : Int {
        return ground
    }

    fun setDinoHit(isDinoHit : Boolean) {
        this.dinoHit = isDinoHit
    }

    fun setDinoRect(dinoRect : Rect) {
        dinoWidth = dinoRect.right - dinoRect.left
        dinoHeight = dinoRect.bottom - dinoRect.top
        this.dinoRect = dinoRect
        ground = dinoRect!!.bottom
    }

    fun setCactusRect(cactusRect : Rect) {
        cactusWidth = cactusRect.right - cactusRect.left
        cactusHeight = cactusRect.bottom - cactusRect.top
        this.cactusRect = cactusRect
    }

    fun moveCactus() {
        if (!dinoHit) { // move left
            cactusRect!!.left -= (cactusSpeed * deltaTime).toInt()
            cactusRect!!.right -= (cactusSpeed * deltaTime).toInt()
        }
    }

    fun resetCactus() {
        cactusRect!!.left = screenRect!!.right
        cactusRect!!.right = cactusRect!!.left + cactusWidth
        cactusRect!!.top = screenRect!!.bottom - cactusHeight
        cactusRect!!.bottom = screenRect!!.bottom
    }

    fun cactusOffScreen() : Boolean {
        return ( cactusRect!!.right < 0 ) || ( cactusRect!!.left > screenRect!!.right )
    }

    fun jump() {
        if (!jumping) {
            jumping = true
            jumpVelocity = jumpForce
        }
    }

    fun dinoHit() : Boolean {
        return dinoRect!!.intersects(
            cactusRect!!.left, cactusRect!!.top,
            cactusRect!!.right, cactusRect!!.bottom)
    }
}