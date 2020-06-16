package com.android.reactionview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

class Board(context: Context) {
    var boardPaint: Paint? = null
    private var currentHeight = BOARD_HEIGHT_NORMAL.toFloat()
    var currentY = BOARD_Y
    var beginHeight = 0f
    var endHeight = 0f
    var beginY = 0f
    var endY = 0f
    private fun initPaint(context: Context) {
        boardPaint = Paint()
        boardPaint!!.isAntiAlias = true
        boardPaint!!.style = Paint.Style.FILL
        boardPaint!!.color = context.resources.getColor(R.color.board)
        boardPaint!!.setShadowLayer(5.0f, 0.0f, 2.0f, -0x1000000)
    }

    fun setCurrentHeight(newHeight: Float) {
        currentHeight = newHeight
        currentY = BOARD_BOTTOM - currentHeight
    }

    fun getCurrentHeight(): Float {
        return currentHeight
    }

    fun drawBoard(canvas: Canvas) {
        val radius = currentHeight / 2
        val board = RectF(
            BOARD_X,
            currentY,
            BOARD_X + BOARD_WIDTH,
            currentY + currentHeight
        )
        canvas.drawRoundRect(board, radius, radius, boardPaint!!)
    }

    companion object {
        val BOARD_WIDTH: Int =
            6 * Emotion.NORMAL_SIZE + 7 * CommonDimen.DIVIDE //DIVIDE = 5dp, Emotion.NORMAL_SIZE = 40dp
        val BOARD_HEIGHT_NORMAL: Int = dpToPx(50)
        val BOARD_HEIGHT_MINIMAL: Int = dpToPx(38)
        const val BOARD_X = 10f
        val BOARD_BOTTOM: Float = (CommonDimen.HEIGHT_VIEW_REACTION - 200).toFloat()
        val BOARD_Y =
            BOARD_BOTTOM - BOARD_HEIGHT_NORMAL
        val BASE_LINE: Float =
            BOARD_Y + Emotion.NORMAL_SIZE + CommonDimen.DIVIDE
    }

    init {
        initPaint(context)
    }
}
