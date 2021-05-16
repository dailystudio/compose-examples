package com.dailystudio.compose.customlayout.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import androidx.compose.ui.unit.dp
import com.dailystudio.compose.customlayout.ui.theme.CustomLayoutTheme
import com.dailystudio.devbricksx.development.Logger

@Composable
fun CustomLayout(
    modifier: Modifier = Modifier,
    content: @Composable() () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            val placeable = measurable.measure(
                // You need set the minWidth/Height of the constraints
                // passed in. Otherwise, your children's measurement
                // will be affected by the parent's modifier.
                // (e.g Modifier.fillMaxHeight())
                constraints.copy(minWidth = 0, minHeight = 0)
            ).also {
                Logger.debug("measured placeable: [${it.width} x ${it.height}]")
            }
            placeable
        }

        val layoutWidth = placeables.maxOf { it.measuredWidth }
        val layoutHeight = placeables.sumBy { it.measuredHeight }
        Logger.debug("measured layout: [${layoutWidth} x ${layoutHeight}]")

        layout(constraints.constrainWidth(layoutWidth), constraints.constrainHeight(layoutHeight)) {
            var yPosition = 0

            placeables.forEach { placeable ->
                placeable.placeRelative(x = 0, y = yPosition)
                yPosition += placeable.height
            }
        }
    }
}

@Preview
@Composable
fun CallingComposable(modifier: Modifier = Modifier) {
    CustomLayoutTheme() {
        CustomLayout(modifier.padding(8.dp).wrapContentHeight()) {
            Text("Now, we have a customized layout, ")
            Text("which places items")
            Text("vertically.")
            Text("You're worth it.")
        }
    }
}