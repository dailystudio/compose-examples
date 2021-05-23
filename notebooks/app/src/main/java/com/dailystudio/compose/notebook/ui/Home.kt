package com.dailystudio.compose.notebook.ui

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.dailystudio.compose.notebook.db.Note
import com.dailystudio.compose.notebook.model.NotebookViewModelExt
import com.dailystudio.devbricksx.development.Logger

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun Home() {
    val navController = rememberNavController()
    val notebookViewModel = viewModel<NotebookViewModelExt>()

    val notebooks by notebookViewModel.allNotebooks.collectAsState(initial = null)
    val notes by notebookViewModel.notesInOpenedNotebook.collectAsState(initial = null)
    val note by notebookViewModel.currentNote.observeAsState(Note.createNote(-1))

    navController.addOnDestinationChangedListener { _, destination, arguments ->
        destination.navigatorName.let {
            if (it.startsWith("notebooks")) {
                notebookViewModel.closeNotebook()
            } else if (it.startsWith("notes")) {
                notebookViewModel.closeNote()
            }
        }
    }

    NavHost(navController = navController,
        startDestination = "notebooks") {
        composable("notebooks") {
            notebookViewModel.closeNotebook()

            NotebooksPage(notebooks,
                onOpenNotebook = {
                    notebookViewModel.openNotebook(it.id)

                    navController.navigate("notes/${it.id}?notebookName=${it.name}")
                },
                onNewNotebook = {
                    notebookViewModel.insertNotebook(it)
                },
                onRemoveNotebooks = {
                    notebookViewModel.deleteNotebooks(it)
                }
            )
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
                NotesPage(notebookId, notebookName, notes,
                    onEditNote = {
                        notebookViewModel.openNote(it.id)
                        navController.navigate("note/${it.id}")
                    },
                    onNewNote = {
                        navController.navigate("note/new/${notebookId}")
                    },
                    onRemoveNotes = {
                        notebookViewModel.deleteNotes(it)
                    }
                )
            }
        }
        composable("note/{noteId}",
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getInt("noteId") ?: -1

            NoteEditScreen(note) {
                Logger.debug("update note: $it")
                notebookViewModel.updateNote(it)

                navController.popBackStack()
            }
        }
        composable("note/new/{notebookId}",
            arguments = listOf(
                navArgument("notebookId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val notebookId = requireNotNull(backStackEntry.arguments?.getInt("notebookId"))

            val newNote = Note.createNote(notebookId)

            NoteEditScreen(newNote) {
                notebookViewModel.insertNote(it)

                navController.popBackStack()
            }
        }
    }
}