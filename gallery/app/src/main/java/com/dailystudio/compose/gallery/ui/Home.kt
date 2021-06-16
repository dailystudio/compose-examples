package com.dailystudio.compose.gallery.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.compose.collectAsLazyPagingItems
import com.dailystudio.compose.gallery.Constants
import com.dailystudio.compose.gallery.api.UnsplashApiInterface
import com.dailystudio.compose.gallery.db.PhotoItemMediator
import com.dailystudio.compose.gallery.R
import com.dailystudio.compose.gallery.model.PhotoItemViewModelExt
import com.dailystudio.devbricksx.development.Logger


const val MENU_ITEM_ID_ABOUT = 0x1

@ExperimentalComposeUiApi
@ExperimentalPagingApi
@ExperimentalFoundationApi
@Composable
fun Home() {
    val viewModel = viewModel<PhotoItemViewModelExt>()

    val queryOfPhotos by viewModel.photoQuery.observeAsState()
    Logger.debug("home recompose: $queryOfPhotos")
    val pager = remember (queryOfPhotos) {
        Pager (
            PagingConfig(pageSize = UnsplashApiInterface.DEFAULT_PER_PAGE),
            remoteMediator = PhotoItemMediator(queryOfPhotos ?: Constants.QUERY_ALL)
        ) {
            viewModel.listPhotos()
        }
    }

    val photos = pager.flow.collectAsLazyPagingItems()
    var showMenu by remember { mutableStateOf(false) }

    var showAboutDialog by remember {
        mutableStateOf(false)
    }

    var searchActivated by remember {
        mutableStateOf(false)
    }

    Scaffold(topBar = {
        val focusRequester = remember { FocusRequester() }
        val keyboardController = LocalSoftwareKeyboardController.current

        val queryValue = queryOfPhotos ?: Constants.QUERY_ALL
        val querySelectionIndex = queryValue.length
        val queryInputState = remember {
            mutableStateOf(
                TextFieldValue(
                    text = queryValue,
                    selection = TextRange(querySelectionIndex)
                )
            )
        }

        if (searchActivated) {
            TopAppBar(
                title = {},
                navigationIcon = {
                    if (searchActivated) {
                        IconButton(onClick = {
                            searchActivated = false
                        }) {
                            Icon(Icons.Default.ArrowBack, "Close Search")
                        }
                    }
                },
                actions = {
                    TextField(
                        value = queryInputState.value,
                        onValueChange = {
                            queryInputState.value = it
                        },
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.hint_title),
                                style = MaterialTheme.typography.h6.copy(
                                    color = Color.White.copy(
                                        alpha = LocalContentAlpha.current
                                    ),
                                ),
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            cursorColor = MaterialTheme.colors.secondaryVariant,
                            backgroundColor = Color.Transparent,
                        ),
                        textStyle = MaterialTheme.typography.h6,
                        keyboardOptions = KeyboardOptions(imeAction = androidx.compose.ui.text.input.ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                var newQuery = queryInputState.value.text
                                if (newQuery.isNullOrBlank()) {
                                    newQuery = Constants.QUERY_ALL
                                }
                                viewModel.searchPhotos(newQuery)
                                keyboardController?.hide()
                                searchActivated = false
                            }
                        ),
                        modifier = Modifier.focusRequester(focusRequester)
                    )

                    DisposableEffect(Unit) {
                        focusRequester.requestFocus()
                        onDispose { }
                    }

                    IconButton(onClick = {
                        showMenu = true
                    }) {
                        Icon(Icons.Default.MoreVert, "More actions")
                    }

                }
            )
        } else {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                actions = {
                    IconButton(onClick = {
                        searchActivated = true
                    }) {
                        Icon(Icons.Default.Search, "Search")
                    }

                    IconButton(onClick = {
                        showMenu = true
                    }) {
                        Icon(Icons.Default.MoreVert, "More actions")
                    }

                    OptionMenus(showMenu = showMenu,
                        onMenuDismissed = { showMenu = false }) {
                        when(it) {
                            MENU_ITEM_ID_ABOUT -> showAboutDialog = true
                        }
                    }
                }
            )
        }

    }) {
        Photos(photos)

        AboutDialog(showDialog = showAboutDialog) {
            showAboutDialog = false
        }

    }
}

@Composable
fun OptionMenus(showMenu: Boolean,
                onMenuDismissed: () -> Unit,
                onMenuItemClick: (Int) -> Unit
) {
    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = onMenuDismissed
    ) {
        DropdownMenuItem(onClick = {
            onMenuItemClick(MENU_ITEM_ID_ABOUT)
            onMenuDismissed()
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