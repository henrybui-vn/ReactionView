package com.android.reactionview

class EaseOutBack(private val duration: Long, private val begin: Float, end: Float) {
    private val s = 1.70158f
    private val change: Float
    fun getCoordinateYFromTime(currentTime: Float): Float {
        var currentTime = currentTime
        return change * ((currentTime / duration - 1.also {
            currentTime = it.toFloat()
        }) * currentTime * ((s + 1) * currentTime + s) + 1) + begin
    }

    companion object {
        fun newInstance(
            duration: Long,
            beginValue: Float,
            endValue: Float
        ): EaseOutBack {
            return EaseOutBack(duration, beginValue, endValue)
        }
    }

    init {
        change = end - begin
    }
}