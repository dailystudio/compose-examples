package com.dailystudio.compose.notebook.ui

import android.view.LayoutInflater
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Article
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.dailystudio.compose.notebook.R
import com.dailystudio.compose.notebook.db.Notebook
import com.dailystudio.compose.notebook.theme.NotesTheme
import com.dailystudio.devbricksx.development.Logger

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun NotebooksPage(notebooks: List<Notebook>?,
                  onOpenNotebook: (Notebook) -> Unit,
                  onNewNotebook: (Notebook) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    var showAboutDialog by remember {
        mutableStateOf(false)
    }

    var showNewNoteDialog by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.app_name))
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
                                Icon(Icons.Filled.Info,
                                    contentDescription = null,
                                    modifier = Modifier.padding(8.dp)
                                )
                                Text(stringResource(id = R.string.menu_about))
                            }
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                showNewNoteDialog = true
            }) {
                Icon(Icons.Filled.Add, contentDescription = null)
            }
        }
    
    ) {
        Notebooks(notebooks = notebooks) {
            Logger.debug("open notebook: $it")
            onOpenNotebook(it)
        }

        AboutDialog(showDialog = showAboutDialog) {
            showAboutDialog = false
        }

        NewNotebookDialog(
            showDialog = showNewNoteDialog,
            onCancel = { showNewNoteDialog = false},
            onNewNotebook = {
                showNewNoteDialog = false

                val newNotebook = Notebook.createNoteBook(it)

                onNewNotebook(newNotebook)
            }
        )
    }
}

@ExperimentalFoundationApi
@Composable
fun Notebooks(notebooks: List<Notebook>?,
              onOpenNotebook: (Notebook) -> Unit) {

    val listState = rememberLazyListState()
    val selectedIndexes = remember{ mutableStateMapOf<Int, Boolean>() }
    var selectionEnabled by remember{ mutableStateOf(false) }

    notebooks?.let {
        LazyColumn(modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp),
            state = listState
        ) {
            items(notebooks) { nb ->
                NotebookItem(
                    notebook = nb,
                    selectable = selectionEnabled,
                    selectedIndexes.containsKey(nb.id),
                    onSelected = {
                        val selected =
                            selectedIndexes.containsKey(nb.id)
                        if (selected) {
                            selectedIndexes.remove(nb.id)
                        } else {
                            selectedIndexes[nb.id] = true
                        }
                    },
                    onItemLongClicked = {
                        if (!selectionEnabled) {
                            selectionEnabled = true
                            selectedIndexes[nb.id] = true
                        }
                    },
                    onItemClicked = onOpenNotebook)
            }
        }
    }
}

@Composable
fun NewNotebookDialog(showDialog: Boolean,
                      onCancel: () -> Unit,
                      onNewNotebook: (String) -> Unit
) {
    var notebookName by remember {
        mutableStateOf("")
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
            },

            title = {
                Text(text = stringResource(id = R.string.dialog_title_new_notebook))
            },
            text = {
                TextField(value = notebookName,
                    onValueChange = {
                        notebookName = it
                    }
                )
            },
            dismissButton = {
                Button(onClick = {
                    onCancel()
                }) {
                    Text(stringResource(id = android.R.string.cancel))
                }
            },
            confirmButton = {
                Button(onClick = {
                    onNewNotebook(notebookName)
                    notebookName = ""
                }) {
                    Text(stringResource(id = android.R.string.ok))
                }
            }
        )
    }
}


@ExperimentalFoundationApi
@Composable
fun NotebookItem(
    notebook: Notebook,
    selectable: Boolean = false,
    selected: Boolean,
    onSelected: (Int) -> Unit,
    onItemClicked: (Notebook) -> Unit,
    onItemLongClicked: (Notebook) -> Unit = {},
    modifier: Modifier = Modifier,

    ) {
    val interactionSource = remember { MutableInteractionSource() }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp)
        ,
        elevation = 4.dp
    ) {
        ConstraintLayout(
            modifier = Modifier
                .combinedClickable(
                    interactionSource = interactionSource,
                    indication = rememberRipple(),
                    onClick = {
                        if (selectable) {
                            onSelected(notebook.id)
                        } else {
                            onItemClicked(notebook)
                        }
                    },
                    onLongClick = {
                        onItemLongClicked(notebook)
                        interactionSource.tryEmit(PressInteraction.Release(PressInteraction.Press(Offset.Zero)))
                    }
                )
        ) {
            val (icon, name, count, indicator) = createRefs()

            Icon(
                tint = MaterialTheme.colors.primary,
                imageVector = Icons.Rounded.Article,
                contentDescription = null,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 10.dp)
                    .constrainAs(icon) {
                        start.linkTo(parent.start)
                        centerVerticallyTo(parent)
                    }
                    .size(44.dp)
            )

            Text(
                text = notebook.name ?: "",
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.subtitle1.copy(
                    textAlign = TextAlign.Left
                ),
                modifier = Modifier
                    .constrainAs(name) {
                        start.linkTo(icon.end)
                        end.linkTo(count.start)
                        width = Dimension.fillToConstraints
                        centerVerticallyTo(parent)
                    }
            )

            Text(
                text = notebook.notesCount.toString(),
                color = MaterialTheme.colors.primaryVariant,
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier
                    .constrainAs(count) {
                        end.linkTo(parent.end, margin = 10.dp)
                        centerVerticallyTo(parent)
                    }
            )


            if (selected) {
                val indicatorColor = MaterialTheme.colors.primary
                Canvas(
                    modifier = Modifier
                        .constrainAs(indicator) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)

                            height = Dimension.fillToConstraints
                        }
                        .width(8.dp)

                ) {
                    drawRect(indicatorColor, size = size)
                }
            }
        }

    }

}

@Preview
@ExperimentalFoundationApi
@Composable
fun NotebookItemPreview() {
    val notebook = Notebook.createNoteBook("Notebook").apply {
        notesCount = 10
    }

    NotesTheme() {
        NotebookItem(notebook, selectable = true, selected = false, {}, {}, {})
    }
}

@Preview
@ExperimentalFoundationApi
@Composable
fun SelectedNotebookItemPreview() {
    val notebook = Notebook.createNoteBook("Notebook").apply {
        notesCount = 10
    }

    NotesTheme() {
        NotebookItem(notebook, selectable = true, selected = true, {}, {}, {})
    }
}

@Preview
@ExperimentalFoundationApi
@Composable
fun EmptyNotebookItemPreview() {
    val notebook = Notebook.createNoteBook("Empty")

    NotesTheme() {
        NotebookItem(notebook, selectable = true, selected = false, {}, {}, {})
    }
}

@Preview
@ExperimentalFoundationApi
@Composable
fun NotebooksPreview() {
    val notebooks = mutableListOf<Notebook>()

    for (i in 0 until 100) {
        notebooks.add(Notebook.createNoteBook("Notebook $i"))
    }

    NotesTheme() {
        Notebooks(notebooks) {}
    }
}
