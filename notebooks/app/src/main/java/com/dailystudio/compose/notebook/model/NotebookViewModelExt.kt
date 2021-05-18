package com.dailystudio.compose.notebook.model

import android.app.Application
import androidx.compose.runtime.*
import androidx.lifecycle.LiveData
import com.dailystudio.compose.notebook.db.Note
import com.dailystudio.compose.notebook.db.Notebook
import com.dailystudio.devbricksx.development.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest

class NotebookViewModelExt(application: Application): NotebookViewModel(application) {

    private var currentNotebookId by mutableStateOf(-1)

    val allNotebooks = getAllNotebooksOrderedByLastModified()
                .mapLatest { notebooks ->
                    val wrapper = mutableListOf<Notebook>()

                    for (notebook in notebooks) {
                        notebook.notesCount = countNotes(notebook.id)
                        Logger.debug("nc: ${notebook.notesCount} of $notebook")

                        wrapper.add(notebook)
                    }

                    wrapper.toList()
                }.flowOn(Dispatchers.IO)

    val notesInOpenedNotebook: LiveData<List<Note>>
        get () {
            return getAllNotesOrderedByLastModifiedLive(currentNotebookId)
        }

    fun openNotebook(notebookId: Int) {
        currentNotebookId = notebookId
    }

}