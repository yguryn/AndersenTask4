package com.example.andersentask4

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class CustomView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0,
) : View(context, attrs, defStyle) {

    private var secondHandColor = Color.BLACK
    private var minuteHandColor = Color.BLACK
    private var hourHandColor = Color.BLACK
    private var secondHandLength = 200f
    private var minuteHandLength = 150f
    private var hourHandLength = 125f
    private val bias = 300f
    private val rect = Rect()
    private val numbers = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)

    private val circlePaint: Paint = Paint().apply {
        color = Color.BLACK
        isAntiAlias = true
        strokeWidth = 21F
        this.style = Paint.Style.STROKE
    }

    private val linePaint: Paint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 10f
    }

    private val textPaint: Paint = Paint().apply {
        color = Color.BLACK
        textSize = 50f
    }

    private val paint: Paint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 10f
    }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.CustomView,
                0, 0).apply {
            try {
                secondHandColor = getColor(R.styleable.CustomView_secondHandColor, Color.BLACK)
                minuteHandColor = getColor(R.styleable.CustomView_minuteHandColor, Color.BLACK)
                hourHandColor = getColor(R.styleable.CustomView_hourHandColor, Color.CYAN)
                secondHandLength = getFloat(R.styleable.CustomView_secondLength, 200f)
                minuteHandLength = getFloat(R.styleable.CustomView_minuteLength, 150f)
                hourHandLength = getFloat(R.styleable.CustomView_hourLength, 125f)
            } finally {
                recycle()
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawCircle((width / 2).toFloat(), (height / 2.0).toFloat(), bias, circlePaint)
        drawDivision(canvas)
        drawNum(canvas)

        val calendar: Calendar = Calendar.getInstance()
        var hour = calendar.get(Calendar.HOUR_OF_DAY)
        hour = if (hour > 12) hour - 12 else hour

        drawHandLine(canvas, (hour + calendar.get(Calendar.MINUTE) / 60) * 5f,
                isHour = true, isSecond = false)
        drawHandLine(canvas, calendar.get(Calendar.MINUTE).toFloat(),
                isHour = false, isSecond = false)
        drawHandLine(canvas, calendar.get(Calendar.SECOND).toFloat(),
                isHour = false, isSecond = true)
        invalidate()
    }

    private fun drawHandLine(canvas: Canvas?, moment: Float, isHour: Boolean, isSecond: Boolean) {
        val angle = Math.PI * moment / 30 - Math.PI / 2
        val arrowSize: Float
        when {
            isHour -> {
                paint.color = hourHandColor
                arrowSize = hourHandLength
            }
            isSecond -> {
                paint.color = secondHandColor
                arrowSize = secondHandLength
            }
            else -> {
                paint.color = minuteHandColor
                arrowSize = minuteHandLength
            }
        }
        canvas?.drawLine((width / 2).toFloat(), (height / 2).toFloat(),
                (width / 2 + cos(angle) * arrowSize).toFloat(),
                (height / 2 + sin(angle) * arrowSize).toFloat(), paint)
        if (isHour) Log.d("Smth", "${(width / 2 + cos(angle) * arrowSize).toFloat()}")
    }

    private fun drawDivision(canvas: Canvas?) {
        for (i in 1..12) {
            canvas?.drawLine((width / 2).toFloat() - bias,
                    (height / 2).toFloat(), (width / 2).toFloat() - bias + 30,
                    (height / 2).toFloat(), linePaint)
            canvas?.rotate(30f, (width / 2).toFloat(), (height / 2).toFloat())
        }
    }

    private fun drawNum(canvas: Canvas?) {
        for (number in numbers) {
            val angle = Math.PI / 6 * (number - 3)
            val temp = number.toString()
            textPaint.getTextBounds(temp, 0, temp.length, rect)
            canvas?.drawText(temp,
                    (width / 2 + cos(angle) * (bias - 60) - rect.width() / 2).toFloat(),
                    (height / 2 + sin(angle) * (bias - 60) + rect.height() / 2).toFloat(),
                    textPaint)
        }
    }
}