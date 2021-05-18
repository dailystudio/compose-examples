package com.dailystudio.compose.notebook.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.dailystudio.compose.notebook.db.Note
import com.dailystudio.compose.notebook.model.NotebookViewModelExt
import com.dailystudio.devbricksx.development.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun Home() {
    val navController = rememberNavController()
    val notebookViewModel = viewModel<NotebookViewModelExt>()

    val notebooks by notebookViewModel.allNotebooks.collectAsState(initial = null)
    val notes by notebookViewModel.notesInOpenedNotebook.observeAsState()

    NavHost(navController = navController,
        startDestination = "notebooks") {
        composable("notebooks") {
            NotebooksPage(notebooks) {
                notebookViewModel.openNotebook(it.id)
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
                NotesPage(notebookName, notes) {
                    navController.navigate("note/${it.id}")
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
                        val noteById = notebookViewModel.getNote(noteId)
                        Logger.debug("noteById: $noteById")

                        noteById?.let { note = it }
                    }
                }
            }

            Logger.debug("use note: $note")
            NoteEditScreen(note) {
                Logger.debug("update note: $it")
                notebookViewModel.updateNote(it)

                navController.popBackStack()
            }
        }
    }
}