package com.maple.gameTimer.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log

class DonutProgress : com.github.lzyzsd.circleprogress.DonutProgress {
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        if (text == null) text = "0"
    }

    private val TAG: String = "DonutProgress"

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        textSize = MeasureSpec.getSize(widthMeasureSpec).toFloat() / 2
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }
}