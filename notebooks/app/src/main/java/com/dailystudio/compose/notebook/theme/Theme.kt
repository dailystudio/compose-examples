
package com.dailystudio.compose.notebook.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val LightColors = lightColors(
    primary = primaryColor,
    primaryVariant = primaryLightColor,
    onPrimary = primaryTextColor,
    surface = primaryTextColor,
    onSurface = primaryColor,
    secondary = secondaryColor,
    secondaryVariant = secondaryLightColor,
    onSecondary = secondaryTextColor,
)

private val DarkColors = darkColors (
    primary = primaryColor,
    primaryVariant = primaryDarkColor,
    onPrimary = primaryTextColor,
    secondary = secondaryColor,
    secondaryVariant = secondaryDarkColor,
    onSecondary = secondaryTextColor,
)

@Composable
fun NotesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (darkTheme) DarkColors else LightColors,
        shapes = shapes,
        content = content
    )
}
