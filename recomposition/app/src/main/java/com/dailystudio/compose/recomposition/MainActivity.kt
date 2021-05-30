package com.dailystudio.compose.recomposition

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dailystudio.compose.recomposition.ui.theme.RecompositionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RecompositionTheme() {
                Scaffold(
                    topBar = {
                        TopAppBar(title = {
                            Text(stringResource(id = R.string.app_name))
                        })
                    }
                ) {
                    var totalClicks by remember { mutableStateOf(0)}

                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ClicksView(totalClicks, { totalClicks++ }, Color.Red, Color.White) {
                            ClicksView(totalClicks,  { totalClicks++ }, Color.Green, Color.Black) {
                                ClicksView(totalClicks, { totalClicks++ }, Color.Blue, Color.White)
                            }
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
               color: Color = Color.White,
               textColor: Color = Color.Black,
               content: (@Composable () -> Unit)? = null
) {
    var areaClicks by remember { mutableStateOf(0)}

    Surface(color = color,
        contentColor = contentColorFor(backgroundColor = color),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                    areaClicks++
                    onTotalClickChange()
                }
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Area clicked $areaClicks / Total clicked $totalClicks",
                color = textColor,
                textAlign = TextAlign.Center,
            )

            if (content != null) {
                Spacer(modifier = Modifier.height(16.dp))

            }

            content?.let { it() }
        }
    }

}
