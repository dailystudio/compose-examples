package com.dailystudio.compose.notebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dailystudio.compose.notebook.db.Note
import com.dailystudio.compose.notebook.db.Notebook
import com.dailystudio.compose.notebook.db.NotesDatabase
import com.dailystudio.compose.notebook.model.NoteViewModel
import com.dailystudio.compose.notebook.model.NotebookViewModel
import com.dailystudio.compose.notebook.theme.NotesTheme
import com.dailystudio.compose.notebook.ui.Notebooks
import com.dailystudio.devbricksx.development.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NotesTheme() {
                Scaffold(
                    topBar = {
                        TopAppBar(title = {
                            Text(text = stringResource(R.string.app_name))
                        })
                    }
                ) {
                    NotebooksPage()
                }

            }
        }

        createSampleNotes(true)
    }

    @Composable
    fun NotebooksPage() {
        val notebookViewModel = ViewModelProvider(this).get(NotebookViewModel::class.java)
        val noteViewModel =
            ViewModelProvider(this).get(NoteViewModel::class.java)
        val notebooks by notebookViewModel.getAllNotebooksOrderedByLastModified()
            .mapLatest { notebooks ->
            val wrapper = mutableListOf<Notebook>()

            for (notebook in notebooks) {

                notebook.notesCount = noteViewModel.countNotes(notebook.id)
                Logger.debug("nc: ${notebook.notesCount} of $notebook")

                wrapper.add(notebook)
            }

            wrapper
        }.flowOn(Dispatchers.IO)
            .collectAsState(
            initial = null
        )

        Notebooks(notebooks = notebooks)
    }
    private fun createSampleNotes(cleanUp: Boolean) {
        lifecycleScope.launch(Dispatchers.IO) {
            val notebooks = arrayOf(
                "Games", "Tech",
                "Books", "Office",
                "Home"
            )

            val maxNotes = 5

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
