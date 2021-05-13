package com.dailystudio.compose.notebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.ui.res.stringResource
import com.dailystudio.compose.notebook.db.Notebook
import com.dailystudio.compose.notebook.theme.NotesTheme
import com.dailystudio.compose.notebook.ui.NotebookItem

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
                    NotebookItem(Notebook.createNoteBook("Hello World"))
                }

            }
        }
    }
}
