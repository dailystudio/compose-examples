package com.dailystudio.compose.gallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.dailystudio.compose.gallery.ui.AboutDialog
import com.dailystudio.compose.gallery.ui.Photos
import com.dailystudio.compose.gallery.ui.theme.GalleryTheme
import com.dailystudio.devbricksx.development.Logger
import java.util.concurrent.Flow

class MainActivity : ComponentActivity() {
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


@ExperimentalPagingApi
@ExperimentalFoundationApi
@Composable
fun Home() {
    val viewModel = viewModel<PhotoItemViewModel>()
    val photos = Pager(
        PagingConfig(pageSize = UnsplashApiInterface.DEFAULT_PER_PAGE),
        remoteMediator = PhotoItemMediator()
    ) {
        viewModel.listPhotos()
    }.flow.collectAsLazyPagingItems()

    var showMenu by remember { mutableStateOf(false) }

    var showAboutDialog by remember {
        mutableStateOf(false)
    }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(text = stringResource(id = R.string.app_name))
            },
            actions = {
                IconButton(onClick = {
                    showMenu = true
                }) {
                    Icon(Icons.Default.MoreVert, "More actions")
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(onClick = {
                        showAboutDialog = true
                        showMenu = false
                    }) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.widthIn(min = 100.dp)
                        ) {
                            Icon(
                                Icons.Filled.Info,
                                contentDescription = null,
                                modifier = Modifier.padding(8.dp)
                            )
                            Text(stringResource(id = R.string.menu_about))
                        }
                    }
                }
            }

        )
    }) {
        Photos(photos)

        AboutDialog(showDialog = showAboutDialog) {
            showAboutDialog = false
        }

    }
}