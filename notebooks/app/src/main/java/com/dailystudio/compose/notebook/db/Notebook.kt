package com.dailystudio.compose.notebook.db

import androidx.room.ForeignKey
import androidx.room.Ignore
import com.dailystudio.devbricksx.annotations.*
import com.dailystudio.devbricksx.database.DateConverter
import com.dailystudio.devbricksx.database.SelectableRecord
import java.util.*

@RoomCompanion(primaryKeys = ["id"],
    autoGenerate = true,
    converters = [DateConverter::class],
    extension = NotebookDaoExtension::class,
    database = "notes",
)
@ViewModel
open class Notebook(id: Int = 0) : SelectableRecord(id) {

    companion object {

        fun createNoteBook(name: String): Notebook {
            return Notebook(0).apply {
                val now = System.currentTimeMillis()

                this.name = name
                this.created = Date(now)
                this.lastModified = this.created
            }
        }

    }

    @JvmField var name: String? = null
    @Ignore var notesCount: Int = 0

    override fun toString(): String {
        return buildString {
            append("Notebook [$id]: $name")
        }
    }
}

@RoomCompanion(primaryKeys = ["id"],
    autoGenerate = true,
    extension = NoteDaoExtension::class,
    database = "notes",
    foreignKeys = [ ForeignKey(entity = Notebook::class,
        parentColumns = ["id"],
        childColumns = ["notebook_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
@ViewModel
class Note(id: Int = 0) : SelectableRecord(id) {

    companion object {

        fun createNote(notebookId: Int = -1,
                       title: String? = null,
                       desc: String? = null): Note {
            return Note(0).apply {
                val now = System.currentTimeMillis()

                this.notebook_id = notebookId
                this.title = title
                this.desc = desc
                this.created = Date(now)
                this.lastModified = this.created
            }
        }

        fun copyNote(note: Note): Note {
            return Note(note.id).apply {
                notebook_id = note.notebook_id
                title = note.title
                desc = note.desc
                created = note.created
                lastModified = note.lastModified
                selected = note.selected
            }
        }

    }

    @JvmField var notebook_id: Int = -1
    @JvmField var title: String? = null
    @JvmField var desc: String? = null

    override fun toString(): String {
        return buildString {
            append("Note [$id, notebook: $notebook_id]: $title, [desc: $desc]")
        }
    }
}
