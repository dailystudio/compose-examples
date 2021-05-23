package com.dailystudio.compose.notebook.db

import androidx.room.ForeignKey
import com.dailystudio.devbricksx.annotations.*
import com.dailystudio.devbricksx.database.DateConverter
import com.dailystudio.devbricksx.database.Record
import java.util.*

@RoomCompanion(primaryKeys = ["id"],
    autoGenerate = true,
    converters = [DateConverter::class],
    extension = NotebookDaoExtension::class,
    database = "notes",
)
@ViewModel(group = "notebook")
open class Notebook(id: Int = 0) : Record(id) {

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

    override fun toString(): String {
        return buildString {
            append("Notebook [$id]: $name")
        }
    }
}

class NotebookInfo(id: Int = 0): Notebook(id) {

    companion object {

        fun createNoteBookInfo(name: String): NotebookInfo {
            return NotebookInfo(0).apply {
                val now = System.currentTimeMillis()

                this.name = name
                this.created = Date(now)
                this.lastModified = this.created
            }
        }

    }

    var notesCount: Int = 0
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
@ViewModel(group = "notebook")
class Note(id: Int = 0) : Record(id) {

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
