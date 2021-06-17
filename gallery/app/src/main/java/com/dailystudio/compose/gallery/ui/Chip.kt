package com.dailystudio.compose.gallery.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dailystudio.devbricksx.development.Logger

@Composable
fun Chip(label: String,
         icon: Painter? = null,
         onChipClick: () -> Unit
) {
    Box(modifier = Modifier
        .padding(vertical = 12.dp)
    ) {
        Surface(
            elevation = 1.dp,
            shape = MaterialTheme.shapes.small.copy(
                CornerSize(25.dp)
            ),
            color = Color.White,
        ) {
            Row(modifier = Modifier
                .clickable {
                    Logger.debug("clicked")
                    onChipClick()
                }.padding(4.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Text(
                    label,
                    modifier = Modifier
                        .absolutePadding(left = 4.dp),
                    style = MaterialTheme.typography.button
                        .copy(color = Color.DarkGray),
                    textAlign = TextAlign.Center
                )
                if (icon != null) Image(
                    icon,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 6.dp),
                    contentDescription = null
                )
            }
        }
    }
}