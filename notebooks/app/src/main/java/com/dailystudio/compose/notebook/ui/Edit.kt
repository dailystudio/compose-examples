package com.dailystudio.compose.notebook.ui

import android.view.textclassifier.TextSelection
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
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
                            .padding(8.dp)
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
            onContentChanged = { noteContent = it.text },
            Modifier.background(Color.White),
        )
    }
}

@Composable
fun NoteEdit(title: String?,
             onTitleChanged: (String) -> Unit,
             content: String?,
             onContentChanged: (TextFieldValue) -> Unit,
             modifier: Modifier = Modifier
) {
    Logger.debug("edit title: $title")
    Logger.debug("edit content: $content")
    val focusRequester = remember { FocusRequester() }

    Column(modifier.padding(16.dp)) {
        TextField(
            value = title ?: "",
            onValueChange = onTitleChanged,
            placeholder = {
                Text(stringResource(id = R.string.hint_title),
                    style = MaterialTheme.typography.h6.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            textStyle = MaterialTheme.typography.h6.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Divider()

        val selection = if (content.isNullOrEmpty()) {
            TextRange.Zero
        } else {
            TextRange(content.length, content.length)
        }

        TextField(
            value = TextFieldValue(content ?: "",
                selection = selection
            ),
            onValueChange = onContentChanged,
            placeholder = {
                TextFieldValue(text = stringResource(id = R.string.hint_content))
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            textStyle = MaterialTheme.typography.subtitle2,
            modifier = Modifier
                .fillMaxSize()
                .focusRequester(focusRequester)
        )

        DisposableEffect(Unit) {
            focusRequester.requestFocus()
            onDispose { }
        }

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
            note,
        ) {

        }
    }
}