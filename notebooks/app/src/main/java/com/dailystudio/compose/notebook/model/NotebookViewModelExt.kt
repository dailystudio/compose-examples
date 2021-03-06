package com.dailystudio.compose.notebook.model

import android.app.Application
import androidx.compose.runtime.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dailystudio.compose.notebook.db.Note
import com.dailystudio.compose.notebook.db.Notebook
import com.dailystudio.devbricksx.development.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch

class NotebookViewModelExt(application: Application): NotebookViewModel(application) {

    private var currentNotebookId by mutableStateOf(-1)
    private var currentNoteId by mutableStateOf(-1)

    private val _currentNote: MutableLiveData<Note> =
        MutableLiveData(Note.createNote())

    val allNotebooks = getAllNotebooksOrderedByLastModified()

    val currentNote: LiveData<Note> = _currentNote

    val notesInOpenedNotebook: Flow<List<Note>>
        get () {
            return getAllNotesOrderedByLastModifiedLive(currentNotebookId)
        }

    fun openNotebook(notebookId: Int) {
        currentNotebookId = notebookId
    }

    fun closeNotebook() {
        currentNotebookId = -1
    }

    fun openNote(id: Int) {
        currentNoteId = id

        if (id == -1) {
            _currentNote.postValue(Note.createNote(currentNotebookId))
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                val note = getNote(currentNoteId)

                note?.let {
                    _currentNote.postValue(note)
                }
            }
        }
    }

    fun closeNote() {
        currentNoteId = -1
        _currentNote.postValue(Note.createNote(currentNotebookId))
    }

    fun deleteNotes(ids: Set<Int>): Job =
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.deleteNotes(ids.toIntArray())
        }

    fun deleteNotebooks(ids: Set<Int>): Job =
        viewModelScope.launch(Dispatchers.IO) {
            notebookRepository.deleteNotebooks(ids.toIntArray())
        }

}