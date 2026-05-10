package com.example.focusguard.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.focusguard.util.ColorProvider

@Composable
fun FocusGuardTheme(
    colorIndex: Int = 0,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val baseColor = ColorProvider.getColor(colorIndex)
    
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = baseColor,
            secondary = baseColor.copy(alpha = 0.7f),
            tertiary = baseColor.copy(alpha = 0.5f),
            background = Color(0xFF121212),
            surface = Color(0xFF1E1E1E),
        )
    } else {
        lightColorScheme(
            primary = baseColor,
            secondary = baseColor.copy(alpha = 0.7f),
            tertiary = baseColor.copy(alpha = 0.5f),
            background = Color(0xFFF5F5F5),
            surface = Color.White,
        )
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
