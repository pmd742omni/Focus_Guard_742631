package com.example.focusguard.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import android.graphics.Color as AndroidColor

object ColorProvider {
    val colors: List<Color> = (0 until 256).map { i ->
        val hue = (i * 360f / 256f)
        Color.hsv(hue, 1f, 1f)
    }

    fun getColor(index: Int): Color {
        return colors.getOrElse(index) { Color.Blue }
    }
}
