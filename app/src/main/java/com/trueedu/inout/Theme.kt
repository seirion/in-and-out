package com.trueedu.inout

import android.graphics.Color

data class Theme(
    val inBg: Int,
    val outBg: Int,
    val inButton: Int,
    val outButton: Int
) {
    companion object {
        // color of developers
        val DEFAULT = Theme(
            Color.RED,
            Color.BLUE,
            Color.RED,
            Color.BLUE
        )
    }
}

