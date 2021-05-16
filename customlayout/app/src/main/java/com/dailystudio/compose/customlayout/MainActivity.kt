package com.dailystudio.compose.customlayout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.dailystudio.compose.customlayout.ui.CustomLayout
import com.dailystudio.compose.customlayout.ui.theme.CustomLayoutTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CustomLayoutTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Home()
                }
            }
        }
    }
}

@Preview
@Composable
fun Home() {
    CustomLayout(modifier = Modifier
        .background(Color.Yellow)
        .fillMaxWidth()
        .wrapContentHeight()
    ) {
        Text("Line1",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Text("Line2")
        Text("Line3")
        Text("Line4")

    }

}
