package com.dailystudio.compose.gallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.dailystudio.compose.gallery.api.UnsplashApiInterface
import com.dailystudio.compose.gallery.db.PhotoItem
import com.dailystudio.compose.gallery.db.PhotoItemMediator
import com.dailystudio.compose.gallery.model.PhotoItemViewModel
import com.dailystudio.compose.gallery.ui.Photos
import com.dailystudio.compose.gallery.ui.theme.GalleryTheme
import com.dailystudio.devbricksx.development.Logger
import java.util.concurrent.Flow

class MainActivity : ComponentActivity() {
    @ExperimentalPagingApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = viewModel<PhotoItemViewModel>()
            val photos = Pager(
                PagingConfig(pageSize = UnsplashApiInterface.DEFAULT_PER_PAGE),
                remoteMediator = PhotoItemMediator()
            ) {
                viewModel.listPhotos()
            }.flow.collectAsLazyPagingItems()

            GalleryTheme {
                Home(photos)
            }
        }
    }
}


@ExperimentalFoundationApi
@Composable
fun Home(photos: LazyPagingItems<PhotoItem>) {
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = stringResource(id = R.string.app_name))
        })
    }) {
        Photos(photos)
    }
}