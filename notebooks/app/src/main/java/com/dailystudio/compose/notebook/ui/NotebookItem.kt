package com.dailystudio.compose.notebook.ui

import android.view.LayoutInflater
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Article
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.dailystudio.compose.notebook.R
import com.dailystudio.compose.notebook.db.Notebook
import com.dailystudio.compose.notebook.theme.NotesTheme
import com.dailystudio.devbricksx.development.Logger

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

@Composable
fun Notebooks(notebooks: List<Notebook>?,
              onOpenNotebook: (Notebook) -> Unit) {
    notebooks?.let {
        LazyColumn(modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(notebooks) { nb ->
                NotebookItem(notebook = nb, onOpenNotebook)
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


@Composable
fun NotebookItem(notebook: Notebook,
                 onItemClicked: (Notebook) -> Unit,
                 modifier: Modifier = Modifier,

) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp),
        elevation = 4.dp
    ) {
        ConstraintLayout(
            modifier = Modifier
                .clickable { onItemClicked(notebook) }
                .padding(10.dp),
        ) {
            val (icon, name, count) = createRefs()

            Icon(
                tint = MaterialTheme.colors.primary,
                imageVector = Icons.Rounded.Article,
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(icon) {
                        start.linkTo(parent.start)
                        centerVerticallyTo(parent)
                    }
                    .size(48.dp)
                    .padding(horizontal = 8.dp)
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
                        end.linkTo(parent.end)
                        centerVerticallyTo(parent)
                    }
                    .padding(horizontal = 8.dp)
            )
        }
    }

}

@Preview
@Composable
fun NotebookItemPreview() {
    val notebook = Notebook.createNoteBook("Notebook").apply {
        notesCount = 10
    }

    NotesTheme() {
        NotebookItem(notebook, {})
    }
}

@Preview
@Composable
fun EmptyNotebookItemPreview() {
    val notebook = Notebook.createNoteBook("Empty")

    NotesTheme() {
        NotebookItem(notebook, {})
    }
}


@Preview
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
