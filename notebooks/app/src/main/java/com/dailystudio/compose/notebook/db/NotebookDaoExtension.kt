package com.dailystudio.compose.notebook.db

import androidx.room.Query
import com.dailystudio.devbricksx.annotations.DaoExtension
import kotlinx.coroutines.flow.Flow

@DaoExtension(entity = Notebook::class)
abstract class NotebookDaoExtension {

    @Query("SELECT notebook.id, notebook.name, notebook.created, notebook.last_modified, count(note.id) as notesCount FROM notebook LEFT OUTER JOIN note ON note.notebook_id = notebook.id GROUP BY notebook.id ORDER BY notebook.last_modified DESC")
    abstract fun getAllNotebooksOrderedByLastModified(): Flow<List<NotebookInfo>>

    @Query("DELETE FROM notebook WHERE id IN (:ids)")
    abstract fun deleteNotebooks(ids: IntArray)

}

@DaoExtension(entity = Note::class)
abstract class NoteDaoExtension {

    @Query("SELECT * FROM note WHERE notebook_id = :notebookId ORDER BY last_modified DESC ")
    abstract fun getAllNotesOrderedByLastModifiedLive(notebookId: Int): Flow<List<Note>>

    @Query("SELECT COUNT(*) FROM note WHERE notebook_id = :notebookId")
    abstract fun countNotes(notebookId: Int): Int

    @Query("DELETE FROM note WHERE id IN (:ids)")
    abstract fun deleteNotes(ids: IntArray)

}