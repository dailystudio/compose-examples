package com.dailystudio.compose.notebook.ui

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.dailystudio.compose.notebook.R
import com.dailystudio.compose.notebook.db.Note
import com.dailystudio.compose.notebook.theme.NotesTheme
import com.dailystudio.devbricksx.development.Logger
import kotlin.math.roundToInt


@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun NotesPage(notebookId: Int,
              notebookName: String,
              notes: List<Note>?,
              onEditNote: (Note) -> Unit,
              onNewNote: () -> Unit,
              onRemoveNotes: (Set<Int>) -> Unit,
) {
    var inSelectionMode by remember {
        mutableStateOf(false)
    }

    val selectedItems = remember {
        mutableStateMapOf<Int, Boolean>()
    }

    val beginSelection = {
        inSelectionMode = true
    }

    val endSelection = {
        inSelectionMode = false
        selectedItems.clear()
    }

    var showDeletionConfirmDialog by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            if (inSelectionMode) {
                TopAppBar(
                    title = {
                        Text(text = stringResource(
                            R.string.prompt_selection,
                            selectedItems.size))
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            endSelection()
                        }) {
                            Icon(Icons.Default.Clear, "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            showDeletionConfirmDialog = true
                        }) {
                            Icon(Icons.Default.Delete, "Delete")
                        }

                    }
                )
            } else {
                TopAppBar(
                    title = {
                        Text(text = notebookName)
                    },
                )
            }
        },
        floatingActionButton = {
            AnimatedVisibility(!inSelectionMode,
                enter = slideInHorizontally(initialOffsetX = {it}),
                exit = slideOutHorizontally(targetOffsetX = { (it * 1.2).roundToInt()})
            ) {
                FloatingActionButton(
                    modifier = Modifier.padding(8.dp),
                    onClick = {
                        onNewNote()
                    }
                ) {
                    Icon(Icons.Filled.Create, contentDescription = null)
                }
            }
        },
    ) {
        BackPressHandler(inSelectionMode) {
            endSelection()
        }

        Notes(notes = notes,
            selectable = inSelectionMode,
            selectedItems = selectedItems.keys,
            onEditNote = onEditNote,
            onSelectNote = {
                Logger.debug("on selected: $it")
                val selected =
                    selectedItems.containsKey(it.id)
                if (selected) {
                    selectedItems.remove(it.id)
                } else {
                    selectedItems[it.id] = true
                }

                Logger.debug("after selected: $selectedItems")
            },
            onSelectionStarted = {
                beginSelection()
                selectedItems[it.id] = true
            }
        )

        DeletionConfirmDialog(
            showDialog = showDeletionConfirmDialog,
            onCancel = { showDeletionConfirmDialog = false }) {
            showDeletionConfirmDialog = false

            onRemoveNotes(selectedItems.keys.toSet())

            endSelection()
        }

    }
}

@ExperimentalFoundationApi
@Composable
fun Notes(notes: List<Note>?,
          selectable: Boolean,
          selectedItems: Set<Int>,
          onEditNote: (Note) -> Unit,
          onSelectionStarted: (Note) -> Unit,
          onSelectNote: (Note) -> Unit,
) {
    val listState = rememberLazyListState()

    notes?.let {
        LazyVerticalGrid(
            cells = GridCells.Fixed(2),
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp),
            state = listState
        ) {
            items(notes) { note ->
                NoteItem(note = note,
                    selectable = selectable,
                    selectedItems.contains(note.id),
                    onItemSelected = {
                        onSelectNote(it)
                    },
                    onItemLongClicked = {
                        if (!selectable) {
                            onSelectionStarted(it)
                        }
                    },
                    onItemClicked = onEditNote)
            }
        }
    }
}


@ExperimentalFoundationApi
@Composable
fun NoteItem(note: Note,
             selectable: Boolean = false,
             selected: Boolean,
             onItemSelected: (Note) -> Unit,
             onItemClicked: (Note) -> Unit,
             onItemLongClicked: (Note) -> Unit = {},
             modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    Card(
        modifier = modifier
            .padding(all = 8.dp),
        elevation = 4.dp
    ) {
        ConstraintLayout(modifier = Modifier
            .combinedClickable(
                interactionSource = interactionSource,
                indication = rememberRipple(),
                onClick = {
                    if (selectable) {
                        onItemSelected(note)
                    } else {
                        onItemClicked(note)
                    }
                },
                onLongClick = {
                    onItemLongClicked(note)
                    interactionSource.tryEmit(PressInteraction.Release(PressInteraction.Press(Offset.Zero)))
                }
            )
            .fillMaxWidth()
            .height(120.dp)
        ) {
            val (content, indicator) = createRefs()

            Column(
                modifier = Modifier
                    .constrainAs(content) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)

                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                    .padding(16.dp)
            ) {
                Text(
                    text = note.title ?: "",
                    style = MaterialTheme.typography.h6.copy(
                        textAlign = TextAlign.Left
                    ),
                )

                Text(
                    text = note.desc ?: "Empty",
                    overflow = TextOverflow.Clip,
                    maxLines = 6,
                    color = Color.Gray,
                    style = MaterialTheme.typography.body2.copy(
                        textAlign = TextAlign.Left
                    ),
                )
            }


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
fun NoteItemPreview() {
    val note = Note.createNote(0, "Note 1", "Blablabla")

    NotesTheme() {
        NoteItem(note, selectable = false, selected = false, {}, {}, {})
    }
}

@Preview
@ExperimentalFoundationApi
@Composable
fun EmptyNoteItemPreview() {
    val note = Note.createNote(0, "Note Empty", null)

    NotesTheme() {
        NoteItem(note, selectable = false, selected = false, {}, {}, {})
    }
}


@Preview
@ExperimentalFoundationApi
@Composable
fun NotesPreview() {
    val notes = mutableListOf<Note>()

    for (i in 0 until 100) {
        notes.add(Note.createNote(i,
            "Note$i",
            if (i % 2 == 0) "Content $i" else "Long content with probably two lines text $i"))
    }

    NotesTheme() {
        Notes(notes, false, setOf(), {}, {}, {})
    }
}
