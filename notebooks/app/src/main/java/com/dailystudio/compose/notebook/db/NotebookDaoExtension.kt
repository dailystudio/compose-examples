package com.dailystudio.compose.notebook.db

import androidx.room.Query
import com.dailystudio.devbricksx.annotations.DaoExtension
import kotlinx.coroutines.flow.Flow

@DaoExtension(entity = Notebook::class)
abstract class NotebookDaoExtension {

    @Query("SELECT * FROM notebook ORDER BY last_modified DESC")
    abstract fun getAllNotebooksOrderedByLastModified(): Flow<List<Notebook>>

}

@DaoExtension(entity = Note::class)
abstract class NoteDaoExtension {

    @Query("SELECT * FROM note WHERE notebook_id = :notebookId ORDER BY last_modified DESC ")
    abstract fun getAllNotesOrderedByLastModifiedLive(notebookId: Int): Flow<List<Note>>

    @Query("SELECT COUNT(*) FROM note WHERE notebook_id = :notebookId")
    abstract fun countNotes(notebookId: Int): Int

}