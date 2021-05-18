package com.dailystudio.compose.notebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.*
import com.dailystudio.compose.notebook.db.Note
import com.dailystudio.compose.notebook.db.Notebook
import com.dailystudio.compose.notebook.db.NotesDatabase
import com.dailystudio.compose.notebook.theme.NotesTheme
import com.dailystudio.compose.notebook.ui.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NotesTheme() {
                Home()
            }
        }

        createSampleNotes(true)
    }

    private fun createSampleNotes(cleanUp: Boolean) {
        lifecycleScope.launch(Dispatchers.IO) {
            val notebooks = arrayOf(
                "Games", "Tech",
                "Books", "Office",
                "Home"
            )

            val maxNotes = 10

            val database = NotesDatabase.getDatabase(
                this@MainActivity)

            if (cleanUp) {
                database.clearAllTables()
            }

            val notes = mutableListOf<Note>()
            for ((i, displayName) in notebooks.withIndex()) {
                val notebookId = i + 1
                val nb = Notebook(notebookId).apply {
                    name = displayName
                    created = Date()
                    lastModified = created
                }

                notes.clear()
                for (j in 0 until maxNotes) {
                    val noteId = notebookId * maxNotes + j + 1

                    notes.add(Note(noteId).apply {
                        notebook_id = notebookId
                        title = "$displayName $j"
                        desc = "Write something for $displayName $j"

                        created = Date()
                        lastModified = created
                    })
                }

                database.notebookDao().insertOrUpdate(nb)
                database.noteDao().insertOrUpdate(notes)
            }

        }
    }

}
