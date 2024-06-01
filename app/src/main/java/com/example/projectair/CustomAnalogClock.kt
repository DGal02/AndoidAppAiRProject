package com.example.projectair

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.AnalogClock
import kotlin.math.cos
import kotlin.math.sin


class CustomAnalogClock : AnalogClock {
    private var paint: Paint? = null
    private var seconds = 0

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    private fun init() {
        paint = Paint()
        paint!!.setColor(Color.RED) // Color of seconds hand
        paint!!.strokeWidth = 5f // Thickness of seconds hand
        paint!!.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Get current seconds
        seconds = (System.currentTimeMillis() / 1000 % 60).toInt()

        // Calculate angle for seconds hand
        val angle = (seconds * 6 * Math.PI / 180).toFloat()

        // Draw seconds hand
        val centerX = width / 2
        val centerY = height / 2
        val handLength = centerX - 20 // Length of seconds hand
        val x = (centerX + handLength * sin(angle.toDouble())).toInt()
        val y = (centerY - handLength * cos(angle.toDouble())).toInt()
        canvas.drawLine(centerX.toFloat(), centerY.toFloat(), x.toFloat(), y.toFloat(), paint!!)

        // Invalidate to redraw
        postInvalidateDelayed(1000)
    }
}

