package com.dailystudio.compose.animateintasstate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.toSize
import com.dailystudio.compose.animateintasstate.ui.theme.AnimateIntAsStateTheme
import kotlin.math.acos
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimateIntAsStateTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    SunRise()
                }
            }
        }
    }
}

@Composable
fun SunRise() {
    var size by remember {
        mutableStateOf(Size(0f, 0f))
    }

    val radiusOfSun = min(size.width, size.height) * .1f
    val widthOfBird = min(size.width, size.height) * .1f

    val startXOfSun = -radiusOfSun
    val endXOfSun = size.width + radiusOfSun

    val xOfSun by animateFloatAsState(targetValue = endXOfSun,
        keyframes {
            durationMillis = 4000
            startXOfSun at 0
            endXOfSun at 4000
        }
    )

    val radiusOfTrackOfSun = size.width / 2 + radiusOfSun
    val yOfSun =
        sin(acos((radiusOfTrackOfSun - xOfSun)
                / radiusOfTrackOfSun)) * radiusOfTrackOfSun


    val startXOfBird = size.width + widthOfBird
    val endXOfBird = -widthOfBird * 2

    val startYOfBird = 0f
    val endYOfBird = 0.6f * size.height

    val xOfBird by animateFloatAsState(targetValue = endXOfBird,
        keyframes {
            durationMillis = 4000
            startXOfBird at 0
            endXOfBird at 4000
        }
    )

    val yOfBird by animateFloatAsState(targetValue = endYOfBird,
        keyframes {
            durationMillis = 4000
            startYOfBird at 0
            endYOfBird at 4000
        }
    )

    Canvas(modifier = Modifier
        .fillMaxSize()
        .onGloballyPositioned { coordinates ->
            size = coordinates.size.toSize()
        }
    ) {
        if (size.isEmpty()) {
            return@Canvas
        }

        val path = Path().apply {
            moveTo(0f, size.height)
            relativeLineTo(size.width * .25f, -size.height * 0.2f)
            relativeLineTo(size.width * .25f, size.height * 0.1f)
            relativeLineTo(size.width * .25f, -size.height * 0.3f)
            lineTo(size.width, size.height)
            close()
        }

        drawCircle(Color.Red, radiusOfSun,
            Offset(xOfSun, size.height * .6f - yOfSun))

        if (xOfBird.roundToInt() % 200 < 100) {
            drawArc(
                Color.Black, 0f, -90f, false,
                Offset(xOfBird, yOfBird),
                Size(widthOfBird, widthOfBird / 2),
                style = Stroke(width = 5f)
            )

            drawArc(
                Color.Black, 180f, 90f, false,
                Offset(xOfBird + widthOfBird, yOfBird),
                Size(widthOfBird, widthOfBird / 2),
                style = Stroke(width = 5f)
            )
        } else {
            drawArc(
                Color.Black, 0f, -100f, false,
                Offset(xOfBird, yOfBird),
                Size(widthOfBird, widthOfBird / 3),
                style = Stroke(width = 4f)
            )

            drawArc(
                Color.Black, 180f, 100f, false,
                Offset(xOfBird + widthOfBird, yOfBird),
                Size(widthOfBird, widthOfBird / 3),
                style = Stroke(width = 4f)
            )
        }
        drawPath(path, Color.Gray)
    }
}
