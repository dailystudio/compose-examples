package com.dailystudio.compose.animations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dailystudio.compose.animations.ui.DemoPageA
import com.dailystudio.compose.animations.ui.DemoPageB
import com.dailystudio.compose.animations.ui.theme.AnimationsTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimationsTheme {
                var current by remember { mutableStateOf("A")}

                var fabVisible by remember {
                    mutableStateOf(false)
                }

                LaunchedEffect(key1 = true) {
                    fabVisible = true
                }

                Scaffold(
                    topBar = {
                        TopAppBar(title = {
                            Text(text = stringResource(id = R.string.app_name))
                        })
                    },
                    floatingActionButton = {
                        AnimatedVisibility (fabVisible,
                            enter = slideInVertically(initialOffsetY = {it}),
                            exit = slideOutVertically(targetOffsetY = { (it * 1.2).roundToInt()})
                        ) {
                            FloatingActionButton(
                                modifier = Modifier.padding(8.dp),
                                onClick = {
                                    current = if (current == "A") "B" else "A"
                                }
                            ) {
                                Icon(Icons.Filled.SwapHoriz, contentDescription = null)
                            }
                        }
                    }
                ) {

                    Crossfade(targetState = current) {
                        when (it) {
                            "A" -> DemoPageA()
                            "B" -> DemoPageB()
                        }
                    }
                }



            }
        }
    }
}
