package com.dailystudio.compose.notebook.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dailystudio.compose.notebook.db.Note
import com.dailystudio.compose.notebook.theme.NotesTheme


@ExperimentalFoundationApi
@Composable
fun Notes(notes: List<Note>?) {
    notes?.let {
        LazyVerticalGrid(
            cells = GridCells.Fixed(2),
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(notes) { note ->
                NoteItem(note = note)
            }
        }
    }
}


@Composable
fun NoteItem(note: Note,
             modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(all = 8.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .clickable { }
                .fillMaxWidth()
                .height(180.dp)
                .padding(10.dp),
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
                style = MaterialTheme.typography.h6.copy(
                    textAlign = TextAlign.Left
                ),
            )
        }
    }

}


@Preview
@Composable
fun NoteItemPreview() {
    val note = Note.createNote(0, "Note 1", "Blablabla")

    NotesTheme() {
        NoteItem(note)
    }
}

@Preview
@Composable
fun EmptyNoteItemPreview() {
    val note = Note.createNote(0, "Note Empty", null)

    NotesTheme() {
        NoteItem(note)
    }
}


@ExperimentalFoundationApi
@Preview
@Composable
fun NotesPreview() {
    val notes = mutableListOf<Note>()

    for (i in 0 until 100) {
        notes.add(Note.createNote(i,
            "Note$i",
            if (i % 2 == 0) "Content $i" else "Long content with probably two lines text $i"))
    }

    NotesTheme() {
        Notes(notes)
    }
}
