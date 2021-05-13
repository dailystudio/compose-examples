
package com.dailystudio.compose.notebook.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.codelab.theming.ui.finish.theme.Brown

private val LightColors = lightColors(
    primary = Brown,
    primaryVariant = Brown,
    onPrimary = Color.White,
    secondary = Brown,
    secondaryVariant = Brown,
    onSecondary = Color.White,
    error = Brown
)

private val DarkColors = darkColors (
    primary = Brown,
    primaryVariant = Brown,
    onPrimary = Color.Black,
    secondary = Brown,
    onSecondary = Color.Black,
    error = Brown
)

@Composable
fun NotesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}
