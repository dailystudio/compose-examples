package com.dailystudio.compose.animations.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dailystudio.compose.animations.R

const val MIN_IMAGE_HEIGHT = 200
const val MAX_IMAGE_HEIGHT = 400
const val IMAGE_HEIGHT_DELTA = 50

@Composable
fun DemoPageA() {
    Column(modifier = Modifier
        .fillMaxSize(),
        verticalArrangement = Arrangement.Center) {
        Text(
            text = stringResource(id = R.string.label_demo_page_a),
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun DemoPageB() {
    Column(modifier = Modifier
        .fillMaxSize()) {
        Text(
            text = stringResource(id = R.string.label_demo_page_b),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
            ,
            textAlign = TextAlign.Center,
        )

        var imageHeight by remember {
            mutableStateOf( MIN_IMAGE_HEIGHT)
        }

        Card(modifier = Modifier
            .padding(horizontal = 16.dp)
            .animateContentSize()
            .clickable {
                imageHeight += IMAGE_HEIGHT_DELTA
                imageHeight = imageHeight.coerceIn(MIN_IMAGE_HEIGHT, MAX_IMAGE_HEIGHT)
            },
            elevation = 8.dp
        ) {
            Image(
                painter = painterResource(id = R.drawable.demo_image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(imageHeight.dp)
            )
        }
    }
}