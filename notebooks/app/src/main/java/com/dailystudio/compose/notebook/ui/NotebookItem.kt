package com.dailystudio.compose.notebook.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dailystudio.compose.notebook.db.Notebook
import com.dailystudio.compose.notebook.theme.NotesTheme

@Composable
fun NotebookItem(notebook: Notebook,
                 modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .clickable {  }
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically

        ) {
            Icon(
                imageVector = Icons.Rounded.Palette,
                contentDescription = null,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Text(
                text = notebook.name ?: "",
                style = MaterialTheme.typography.h5,
            )

            Text(
                text = notebook.notesCount.toString(),
                style = MaterialTheme.typography.h6,
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
        NotebookItem(notebook)
    }
}

@Preview
@Composable
fun EmptyNotebookItemPreview() {
    val notebook = Notebook.createNoteBook("Empty")

    NotesTheme() {
        NotebookItem(notebook)
    }
}
