package com.dailystudio.compose.gallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.paging.ExperimentalPagingApi
import com.dailystudio.compose.gallery.ui.Home
import com.dailystudio.compose.gallery.ui.theme.GalleryTheme

class MainActivity : ComponentActivity() {

    @ExperimentalComposeUiApi
    @ExperimentalFoundationApi
    @ExperimentalPagingApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GalleryTheme {
                Home()
            }
        }
    }
}

