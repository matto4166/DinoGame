package com.example.dinogame

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View

class GameView : View {
    private var width = 0
    private var height = 0
    private var paint : Paint = Paint()
    private lateinit var screenRect : Rect
    private lateinit var dino : Bitmap
    private lateinit var dinoRect : Rect
    private lateinit var cactus : Bitmap
    private lateinit var cactusRect : Rect
    private lateinit var dinoGame : DinoGame

    constructor(context : Context, width : Int, height : Int, dinoGame: DinoGame) : super(context) {
        this.width = width
        this.height = height

        paint.isAntiAlias = true

        screenRect = Rect(0,0,width,height)

        dino = BitmapFactory.decodeResource(resources, R.drawable.cute_dino)
        dinoRect = Rect((width * 0.05f).toInt(), height - (height * 0.2f).toInt(), (width * 0.15f).toInt(), height)
        cactus = BitmapFactory.decodeResource(resources, R.drawable.cactus)
        cactusRect = Rect(width - (width * 0.1f).toInt(), height - (height * 0.4f).toInt(), width, height)
        this.dinoGame = dinoGame

        dinoGame.setParams(screenRect, dinoRect, cactusRect, width.toFloat() / 4000, 100f)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawBitmap(dino, null, dinoGame.getDinoRect(), paint)
        canvas.drawBitmap(cactus, null, dinoGame.getCactusRect(), paint)
    }
}