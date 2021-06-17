package com.dailystudio.compose.gallery.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.DpOffset
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
        Logger.debug("queryValue recompose: $queryValue")

        val queryInputState = mutableStateOf(
            TextFieldValue(
                text = if (queryValue == Constants.QUERY_ALL) {
                    ""
                } else {
                    queryValue
                },
                selection = TextRange(querySelectionIndex)
            )
        )


        if (searchActivated) {
            TopAppBar(
                title = {
//                    Text(text = stringResource(id = R.string.app_name))
                },
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
                    var xOffsetOfSearchInPx = 0f
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
                                if (newQuery.isBlank()) {
                                    newQuery = Constants.QUERY_ALL
                                }
                                viewModel.searchPhotos(newQuery)
                                keyboardController?.hide()
                                searchActivated = false
                            }
                        ),
                        modifier = Modifier.focusRequester(focusRequester)
                            .fillMaxWidth(.8f)
                            .onGloballyPositioned {
                                xOffsetOfSearchInPx = it.positionInRoot().x
                            }
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

                    val density = LocalDensity.current.density
                    var marginToEndOfScreenInPx = with(LocalDensity.current) {
                        (LocalConfiguration.current.screenWidthDp.dp.roundToPx() - xOffsetOfSearchInPx)
                    }

                    OptionMenus(modifier = Modifier.onGloballyPositioned {
                            marginToEndOfScreenInPx -= it.size.width
                        },
                        menuOffset = DpOffset(
                            (marginToEndOfScreenInPx / density).dp,
                            0.dp
                        ),
                        showMenu = showMenu,
                        onMenuDismissed = { showMenu = false }) {
                        when(it) {
                            MENU_ITEM_ID_ABOUT -> showAboutDialog = true
                        }
                    }

                }
            )
        } else {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                actions = {
                    var xOffsetOfSearchInPx = 0f
                    IconButton(
                        onClick = {
                            searchActivated = true
                        },
                        modifier = Modifier.onGloballyPositioned {
                            xOffsetOfSearchInPx = it.positionInRoot().x
                        }
                    ) {
                        Icon(Icons.Default.Search, "Search")
                    }

                    if (queryOfPhotos != Constants.QUERY_ALL) {
                        Chip(
                            label = queryInputState.value.text,
                            icon = painterResource(id = R.drawable.ic_remove_search)
                        ) {
                            Logger.debug("clear search")
                            viewModel.searchPhotos(Constants.QUERY_ALL)
                        }
                    }

                    IconButton(onClick = {
                        showMenu = true
                    }) {
                        Icon(Icons.Default.MoreVert, "More actions")
                    }

                    val density = LocalDensity.current.density
                    var marginToEndOfScreenInPx = with(LocalDensity.current) {
                        (LocalConfiguration.current.screenWidthDp.dp.roundToPx() - xOffsetOfSearchInPx)
                    }

                    OptionMenus(modifier = Modifier.onGloballyPositioned {
                        marginToEndOfScreenInPx -= it.size.width
                    },
                        menuOffset = DpOffset(
                            (marginToEndOfScreenInPx / density).dp,
                            0.dp
                        ),
                        showMenu = showMenu,
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
fun OptionMenus(modifier: Modifier = Modifier,
                showMenu: Boolean,
                menuOffset: DpOffset = DpOffset.Zero,
                onMenuDismissed: () -> Unit,
                onMenuItemClick: (Int) -> Unit
) {
    DropdownMenu(
        modifier = modifier,
        offset = menuOffset,
        expanded = showMenu,
        onDismissRequest = onMenuDismissed,
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