package com.android.reactionview

import android.content.Context
import android.graphics.*
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView

class Emotion(
    private val context: Context,
    title: String,
    imageResource: Int
) {
    var currentSize = NORMAL_SIZE
    var beginSize = 0
    var endSize = 0
    var currentX = 0f
    var currentY = 0f
    var beginY = 0f
    var endY = 0f
    var imageOrigin: Bitmap
    lateinit var imageTitle: Bitmap
    var emotionPaint: Paint
    var titlePaint: Paint
    private var ratioWH = 0f
    private fun generateTitleView(title: String) {
        val inflater = LayoutInflater.from(context)
        val titleView: View = inflater.inflate(R.layout.title, null)
        (titleView as TextView).text = title
        val w = context.resources.getDimension(R.dimen.width_title).toInt()
        val h = context.resources.getDimension(R.dimen.height_title).toInt()
        ratioWH = w * 1.0f / (h * 1.0f)
        imageTitle = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val c = Canvas(imageTitle)
        titleView.layout(0, 0, w, h)
        titleView.paint.isAntiAlias = true
        titleView.draw(c)
    }

    fun setAlphaTitle(alpha: Int) {
        titlePaint.alpha = alpha
    }

    fun drawEmotion(canvas: Canvas) {
        canvas.drawBitmap(
            imageOrigin,
            null,
            RectF(currentX, currentY, currentX + currentSize, currentY + currentSize),
            emotionPaint
        )
        drawTitle(canvas)
    }

    fun drawTitle(canvas: Canvas) {
        val width = (currentSize - NORMAL_SIZE) * 7 / 6
        val height = (width / ratioWH).toInt()
        setAlphaTitle(
            Math.min(
                CommonDimen.MAX_ALPHA * width / MAX_WIDTH_TITLE,
                CommonDimen.MAX_ALPHA
            )
        )
        if (width <= 0 || height <= 0) return
        val x = currentX + (currentSize - width) / 2
        val y = currentY - DISTANCE - height
        canvas.drawBitmap(imageTitle, null, RectF(x, y, x + width, y + height), titlePaint)
    }

    companion object {
        val MINIMAL_SIZE: Int = dpToPx(28)
        val NORMAL_SIZE: Int = dpToPx(40)
        val CHOOSE_SIZE: Int = dpToPx(100)
        val DISTANCE: Int = dpToPx(15)
        val MAX_WIDTH_TITLE: Int = dpToPx(70)
    }

    init {
        imageOrigin = BitmapFactory.decodeResource(context.resources, imageResource)
        emotionPaint = Paint(Paint.FILTER_BITMAP_FLAG)
        emotionPaint.isAntiAlias = true
        titlePaint = Paint(Paint.FILTER_BITMAP_FLAG)
        titlePaint.isAntiAlias = true
        generateTitleView(title)
    }
}
