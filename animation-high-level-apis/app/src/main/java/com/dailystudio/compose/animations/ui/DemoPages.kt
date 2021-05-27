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

const val COLLAPSE_ARTICLE_HEIGHT = 85
const val EXPAND_ARTICLE_HEIGHT = 200

@Composable
fun DemoPageA() {
    Column(modifier = Modifier
        .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.label_demo_page_a),
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.primary,

            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            textAlign = TextAlign.Center,
        )

        var articleHeight by remember {
            mutableStateOf( COLLAPSE_ARTICLE_HEIGHT )
        }

        Card(modifier = Modifier
            .padding(horizontal = 16.dp),
            elevation = 16.dp
        ) {
            Text(
                text = stringResource(id = R.string.article),
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .height(articleHeight.dp + 16.dp)
                    .clickable {
                        articleHeight = if (articleHeight == COLLAPSE_ARTICLE_HEIGHT) {
                            EXPAND_ARTICLE_HEIGHT
                        } else {
                            COLLAPSE_ARTICLE_HEIGHT
                        }
                    }
                    .padding(8.dp)
                    .animateContentSize()
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun DemoPageB() {
    Column(modifier = Modifier
        .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.label_demo_page_b),
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
            ,
            textAlign = TextAlign.Center,
        )

        var imageHeight by remember {
            mutableStateOf( MIN_IMAGE_HEIGHT )
        }

        Card(modifier = Modifier
            .animateContentSize()
            .padding(horizontal = 16.dp)
            .height(imageHeight.dp)
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
            )
        }
    }
}