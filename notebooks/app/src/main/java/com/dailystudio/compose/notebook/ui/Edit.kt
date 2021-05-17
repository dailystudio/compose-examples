package com.dailystudio.compose.notebook.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Article
import androidx.compose.material.icons.rounded.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dailystudio.compose.notebook.R
import com.dailystudio.compose.notebook.db.Note
import com.dailystudio.compose.notebook.theme.NotesTheme
import com.dailystudio.devbricksx.development.Logger

@Composable
fun NoteEditScreen(note: Note,
                   onEditCompleted: (Note) -> Unit) {
    Logger.debug("edit note: $note")

    var noteTitle by remember(note.id) { mutableStateOf(note.title) }
    var noteContent by remember(note.id) { mutableStateOf(note.desc) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable {
                                val updatedNote = Note.copyNote(note).apply {
                                    title = noteTitle
                                    desc = noteContent
                                }

                                Logger.debug("complete edit: $updatedNote")
                                onEditCompleted(updatedNote)
                            }
                            .padding(8.dp)
                    )
                }
            )
        }
    ) {

        NoteEdit(
            title = noteTitle,
            onTitleChanged = { noteTitle = it },
            content = noteContent,
            onContentChanged = { noteContent = it},
            Modifier.background(Color.White),
        )
    }
}

@Composable
fun NoteEdit(title: String?,
             onTitleChanged: (String) -> Unit,
             content: String?,
             onContentChanged: (String) -> Unit,
             modifier: Modifier = Modifier
) {
    Logger.debug("edit title: $title")
    Logger.debug("edit content: $content")

    Column(modifier) {
        TextField(
            value = title ?: "",
            onValueChange = onTitleChanged,
            placeholder = {
                Text(stringResource(id = R.string.hint_title))
            },
            modifier = Modifier.fillMaxWidth()
        )

        Divider()

        TextField(
            value = content ?: "",
            onValueChange = onContentChanged,
            placeholder = {
                Text(stringResource(id = R.string.hint_content))
            },
            modifier = Modifier
                .fillMaxSize()
        )

    }
}

@Preview
@Composable
fun NoteEditPreview() {
    NotesTheme(
    ) {
        val note = Note.createNote(0,
            "Hello World",
            "This is your first note.")
        NoteEditScreen(
            note
        ) {

        }
    }
}