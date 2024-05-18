package com.example.chronoheist
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.core.content.ContextCompat


class PopUpMenu(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    private val paint = Paint().apply {
        color = Color.LTGRAY
        style = Paint.Style.FILL
    }

    private val resumeBtn: Button = Button(context).apply {
        id = View.generateViewId()
        text = "Resume"
        setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_blue_light))
    }

    private val optionsBtn: Button = Button(context).apply {
        id = View.generateViewId()
        text = "Options"
        setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_green_light))
    }

    private val exitBtn: Button = Button(context).apply {
        id = View.generateViewId()
        text = "Exit Game"
        setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_red_light))
    }

    init {
        addView(resumeBtn)
        addView(optionsBtn)
        addView(exitBtn)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Define the rectangle dimensions
        val left = (width * 0.1).toFloat()
        val top = (height * 0.1).toFloat()
        val right = (width * 0.9).toFloat()
        val bottom = (height * 0.9).toFloat()

        // Draw the rectangle
        canvas.drawRect(left, top, right, bottom, paint)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        // Calculate the dimensions and position for the rectangle
        val rectLeft = (width * 0.1).toInt()
        val rectTop = (height * 0.1).toInt()
        val rectRight = (width * 0.9).toInt()
        val rectBottom = (height * 0.9).toInt()

        // Calculate button sizes and positions
        val buttonWidth = (rectRight - rectLeft) / 2
        val buttonHeight = (rectBottom - rectTop) / 10
        val buttonSpacing = buttonHeight / 2

        // Position buttons within the rectangle
        resumeBtn.layout(
            rectLeft + (rectRight - rectLeft) / 2 - buttonWidth / 2,
            rectTop + buttonSpacing,
            rectLeft + (rectRight - rectLeft) / 2 + buttonWidth / 2,
            rectTop + buttonSpacing + buttonHeight
        )

        optionsBtn.layout(
            rectLeft + (rectRight - rectLeft) / 2 - buttonWidth / 2,
            rectTop + 2 * buttonSpacing + buttonHeight,
            rectLeft + (rectRight - rectLeft) / 2 + buttonWidth / 2,
            rectTop + 2 * buttonSpacing + 2 * buttonHeight
        )

        exitBtn.layout(
            rectLeft + (rectRight - rectLeft) / 2 - buttonWidth / 2,
            rectTop + 3 * buttonSpacing + 2 * buttonHeight,
            rectLeft + (rectRight - rectLeft) / 2 + buttonWidth / 2,
            rectTop + 3 * buttonSpacing + 3 * buttonHeight
        )
    }
}
