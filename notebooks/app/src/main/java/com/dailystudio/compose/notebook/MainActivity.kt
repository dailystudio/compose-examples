package com.dailystudio.compose.notebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.dailystudio.compose.notebook.db.Note
import com.dailystudio.compose.notebook.db.Notebook
import com.dailystudio.compose.notebook.db.NotesDatabase
import com.dailystudio.compose.notebook.model.NoteViewModel
import com.dailystudio.compose.notebook.model.NotebookViewModel
import com.dailystudio.compose.notebook.theme.NotesTheme
import com.dailystudio.compose.notebook.ui.*
import com.dailystudio.devbricksx.development.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainActivity : AppCompatActivity() {

    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NotesTheme() {
                val navController = rememberNavController()
                val notebookViewModel by viewModels<NotebookViewModel>()
                val noteViewModel by viewModels<NoteViewModel>()

                NavHost(navController = navController,
                    startDestination = "notebooks") {
                    composable("notebooks") {
                        val flow = notebookViewModel.getAllNotebooksOrderedByLastModified()
                            .mapLatest { notebooks ->
                                val wrapper = mutableListOf<Notebook>()

                                for (notebook in notebooks) {

                                    notebook.notesCount = noteViewModel.countNotes(notebook.id)
                                    Logger.debug("nc: ${notebook.notesCount} of $notebook")

                                    wrapper.add(notebook)
                                }

                                wrapper
                            }.flowOn(Dispatchers.IO)

                        val notebooks by flow.collectAsState(initial = null)
                        NotebooksPage(notebooks) {
                            navController.navigate("notes/${it.id}?notebookName=${it.name}")
                        }
                    }
                    composable("notes/{notebookId}?notebookName={notebookName}",
                        arguments = listOf(
                            navArgument("notebookId") {
                                type = NavType.IntType
                            },
                            navArgument("notebookName") {
                                type = NavType.StringType
                            }
                        )
                    ) { backStackEntry ->
                        val notebookId = backStackEntry.arguments?.getInt("notebookId")
                        val notebookName = backStackEntry.arguments?.getString("notebookName")
                        if (notebookId != null && notebookName != null) {
                            val notes by noteViewModel.getAllNotesOrderedByLastModifiedLive(notebookId)
                                .observeAsState()
                            NotesPage(notebookName, notes) {
                                navController.navigate("note/${it.id}") {
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                    composable("note/{noteId}",
                        arguments = listOf(
                            navArgument("noteId") {
                                type = NavType.IntType
                            }
                        )
                    ) { backStackEntry ->
                        val noteId = backStackEntry.arguments?.getInt("noteId")

                        var note by remember { mutableStateOf( Note.createNote() ) }
                        LaunchedEffect(key1 = noteId) {
                            launch(Dispatchers.IO) {
                                if (noteId != null) {
                                    val noteById = noteViewModel.getNote(noteId)
                                    Logger.debug("noteById: $noteById")

                                    noteById?.let { note = it }
                                }
                            }
                        }

                        Logger.debug("use note: $note")
                        NoteEditScreen(note) {
                            Logger.debug("update note: $it")

                            noteViewModel.updateNote(it)

                            navController.popBackStack()
                        }
                    }
                }
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
