package com.android.reactionview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import com.android.reactionview.CommonDimen.DIVIDE

class ReactionView : View {
    internal enum class StateDraw {
        BEGIN, CHOOSING, NORMAL
    }

    private lateinit var easeOutBack: EaseOutBack
    private lateinit var board: Board
    private var emotions: Array<Emotion> = emptyArray()
    private var state: StateDraw? = StateDraw.BEGIN
    private var currentPosition = 0

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
        init()
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        board = Board(context)
        setLayerType(LAYER_TYPE_SOFTWARE, board.boardPaint)
        val array = mutableListOf<Emotion>()
        array.add(Emotion(context, "Like", R.drawable.like))
        array.add(Emotion(context, "Love", R.drawable.love))
        array.add(Emotion(context, "Haha", R.drawable.haha))
        array.add(Emotion(context, "Wow", R.drawable.wow))
        array.add(Emotion(context, "Cry", R.drawable.cry))
        array.add(Emotion(context, "Angry", R.drawable.angry))
        emotions = array.toTypedArray()
        initElement()
    }

    private fun initElement() {
        board.currentY = (CommonDimen.HEIGHT_VIEW_REACTION + 10).toFloat()
        for (e in emotions) {
            e.currentY = board.currentY + CommonDimen.DIVIDE
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (state != null) {
            board.drawBoard(canvas)
            for (emotion in emotions) {
                emotion.drawEmotion(canvas)
            }
        }
    }

    private fun beforeAnimateBeginning() {
        board.beginHeight = Board.BOARD_HEIGHT_NORMAL.toFloat()
        board.endHeight = Board.BOARD_HEIGHT_NORMAL.toFloat()
        board.beginY = Board.BOARD_BOTTOM + 150
        board.endY = Board.BOARD_Y
        easeOutBack = EaseOutBack.newInstance(
            DURATION_BEGINNING_EACH_ITEM,
            Math.abs(board.beginY - board.endY),
            0F
        )
        for (i in emotions.indices) {
            emotions[i].endY = Board.BASE_LINE - Emotion.NORMAL_SIZE
            emotions[i].beginY = Board.BOARD_BOTTOM + 150
            emotions[i].currentX =
                if (i == 0) Board.BOARD_X + DIVIDE else emotions[i - 1].currentX + emotions[i - 1].currentSize + DIVIDE
        }
    }

    private fun beforeAnimateChoosing() {
        board.beginHeight = board.getCurrentHeight()
        board.endHeight = Board.BOARD_HEIGHT_MINIMAL.toFloat()
        for (i in emotions.indices) {
            emotions[i].beginSize = emotions[i].currentSize
            if (i == currentPosition) {
                emotions[i].endSize = Emotion.CHOOSE_SIZE
            } else {
                emotions[i].endSize = Emotion.MINIMAL_SIZE
            }
        }
    }

    private fun beforeAnimateNormalBack() {
        board.beginHeight = board.getCurrentHeight()
        board.endHeight = Board.BOARD_HEIGHT_NORMAL.toFloat()
        for (i in emotions.indices) {
            emotions[i].beginSize = emotions[i].currentSize
            emotions[i].endSize = Emotion.NORMAL_SIZE
        }
    }

    private fun calculateInSessionChoosingAndEnding(interpolatedTime: Float) {
        board.setCurrentHeight(board.beginHeight + (interpolatedTime * (board.endHeight - board.beginHeight)))
        for (i in emotions.indices) {
            emotions[i].currentSize = calculateSize(i, interpolatedTime)
            emotions[i].currentY = Board.BASE_LINE - emotions[i].currentSize
        }
        calculateCoordinateX()
        invalidate()
    }

    private fun calculateInSessionBeginning(interpolatedTime: Float) {
        val currentTime =
            interpolatedTime * DURATION_BEGINNING_ANIMATION
        if (currentTime > 0) {
            board.currentY = board.endY + easeOutBack.getCoordinateYFromTime(
                Math.min(
                    currentTime,
                    DURATION_BEGINNING_EACH_ITEM.toFloat()
                )
            )
        }
        if (currentTime >= 100) {
            emotions[0].currentY = emotions[0].endY + easeOutBack.getCoordinateYFromTime(
                Math.min(
                    currentTime - 100,
                    DURATION_BEGINNING_EACH_ITEM.toFloat()
                )
            )
        }
        if (currentTime >= 200) {
            emotions[1].currentY = emotions[1].endY + easeOutBack.getCoordinateYFromTime(
                Math.min(
                    currentTime - 200,
                    DURATION_BEGINNING_EACH_ITEM.toFloat()
                )
            )
        }
        if (currentTime >= 300) {
            emotions[2].currentY = emotions[2].endY + easeOutBack.getCoordinateYFromTime(
                Math.min(
                    currentTime - 300,
                    DURATION_BEGINNING_EACH_ITEM.toFloat()
                )
            )
        }
        if (currentTime >= 400) {
            emotions[3].currentY = emotions[3].endY + easeOutBack.getCoordinateYFromTime(
                Math.min(
                    currentTime - 400,
                    DURATION_BEGINNING_EACH_ITEM.toFloat()
                )
            )
        }
        if (currentTime >= 500) {
            emotions[4].currentY = emotions[4].endY + easeOutBack.getCoordinateYFromTime(
                Math.min(
                    currentTime - 500,
                    DURATION_BEGINNING_EACH_ITEM.toFloat()
                )
            )
        }
        if (currentTime >= 600) {
            emotions[5].currentY = emotions[5].endY + easeOutBack.getCoordinateYFromTime(
                Math.min(
                    currentTime - 600,
                    DURATION_BEGINNING_EACH_ITEM.toFloat()
                )
            )
        }
        invalidate()
    }

    private fun calculateSize(position: Int, interpolatedTime: Float): Int {
        val changeSize: Int = emotions[position].endSize - emotions[position].beginSize
        return emotions[position].beginSize + (interpolatedTime * changeSize).toInt()
    }

    private fun calculateCoordinateX() {
        emotions[0].currentX = Board.BOARD_X + DIVIDE
        emotions[emotions.size - 1].currentX =
            Board.BOARD_X + Board.BOARD_WIDTH - DIVIDE - emotions[emotions.size - 1].currentSize
        for (i in 1 until currentPosition) {
            emotions[i].currentX =
                emotions[i - 1].currentX + emotions[i - 1].currentSize + DIVIDE
        }
        for (i in emotions.size - 2 downTo currentPosition + 1) {
            emotions[i].currentX =
                emotions[i + 1].currentX - emotions[i].currentSize - DIVIDE
        }
        if (currentPosition != 0 && currentPosition != emotions.size - 1) {
            if (currentPosition <= emotions.size / 2 - 1) {
                emotions[currentPosition].currentX =
                    emotions[currentPosition - 1].currentX + emotions[currentPosition - 1].currentSize + DIVIDE
            } else {
                emotions[currentPosition].currentX =
                    emotions[currentPosition + 1].currentX - emotions[currentPosition].currentSize - DIVIDE
            }
        }
    }

    fun show() {
        state = StateDraw.BEGIN
        visibility = VISIBLE
        beforeAnimateBeginning()
        startAnimation(BeginningAnimation())
    }

    private fun selected(position: Int) {
        if (currentPosition == position && state == StateDraw.CHOOSING) return
        state = StateDraw.CHOOSING
        currentPosition = position
        startAnimation(ChooseEmotionAnimation())
    }

    fun backToNormal() {
        state = StateDraw.NORMAL
        startAnimation(ChooseEmotionAnimation())
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var handled = false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> handled = true
            MotionEvent.ACTION_MOVE -> {
                var i = 0
                while (i < emotions.size) {
                    if (event.x > emotions[i].currentX && event.x < emotions[i].currentX + emotions[i].currentSize
                    ) {
                        selected(i)
                        break
                    }
                    i++
                }
                handled = true
            }
            MotionEvent.ACTION_UP -> {
                backToNormal()
                handled = true
            }
        }
        return handled
    }

    internal inner class ChooseEmotionAnimation : Animation() {
        override fun applyTransformation(
            interpolatedTime: Float,
            t: Transformation
        ) {
            calculateInSessionChoosingAndEnding(interpolatedTime)
        }

        init {
            if (state == StateDraw.CHOOSING) {
                beforeAnimateChoosing()
            } else if (state == StateDraw.NORMAL) {
                beforeAnimateNormalBack()
            }
            duration = DURATION_ANIMATION
        }
    }

    internal inner class BeginningAnimation : Animation() {
        override fun applyTransformation(
            interpolatedTime: Float,
            t: Transformation
        ) {
            calculateInSessionBeginning(interpolatedTime)
        }

        init {
            beforeAnimateBeginning()
            duration = DURATION_BEGINNING_ANIMATION
        }
    }

    companion object {
        const val DURATION_ANIMATION: Long = 200
        const val DURATION_BEGINNING_EACH_ITEM: Long = 300
        const val DURATION_BEGINNING_ANIMATION: Long = 900
    }
}
