package com.android.reactionview

import android.content.res.Resources

fun dpToPx(dp: Int): Int {
    return (dp * Resources.getSystem().displayMetrics.density).toInt()
}
