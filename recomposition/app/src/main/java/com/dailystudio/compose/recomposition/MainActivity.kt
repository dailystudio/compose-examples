package com.dailystudio.compose.recomposition

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dailystudio.compose.recomposition.ui.theme.RecompositionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            RecompositionTheme {
                var totalClicks by remember { mutableStateOf(0)}

                ClicksView(totalClicks, { totalClicks++ }, "Level 1", Color.Red) {
                    ClicksView(totalClicks,  { totalClicks++ },"Level 2", Color.Green) {
                        ClicksView(totalClicks, { totalClicks++ },"Level 3", Color.Blue) {

                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ClicksView(totalClicks: Int,
               onTotalClickChange: () -> Unit,
               name: String,
               color: Color = Color.White,
               content: @Composable () -> Unit
) {
    var clicks by remember { mutableStateOf(0)}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                clicks++
                onTotalClickChange()
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(color = color,
            modifier = Modifier.fillMaxWidth()) {
            Text(text = "[$name] clicked $clicks / Total clicked $totalClicks",
                Modifier.padding(48.dp),
                color = LocalContentColor.current,
                textAlign = TextAlign.Center
            )
        }
        content()
    }
}
