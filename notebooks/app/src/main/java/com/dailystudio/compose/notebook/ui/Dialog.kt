package com.dailystudio.compose.notebook.ui

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dailystudio.compose.notebook.R

@Composable
fun NewNotebookDialog(showDialog: Boolean,
                      onCancel: () -> Unit,
                      onNewNotebook: (String) -> Unit
) {
    var notebookName by remember {
        mutableStateOf("")
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
            },

            title = {
                Text(
                    text = stringResource(id = R.string.dialog_title_new_notebook),
                    style = MaterialTheme.typography.h5.copy(
                        fontWeight = FontWeight.Bold
                    ),
                )
            },

            text = {
                TextField(
                    value = notebookName,
                    onValueChange = {
                        notebookName = it
                    },
                    placeholder = {
                        Text(text = stringResource(id = R.string.hint_new_notebook),
                            style = MaterialTheme.typography.h6)
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent
                    ),
                    textStyle = MaterialTheme.typography.h6,
                )
            },
            dismissButton = {
                TextButton(onClick = {
                    onCancel()
                }) {
                    Text(stringResource(id = android.R.string.cancel))
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    onNewNotebook(notebookName)
                    notebookName = ""
                }) {
                    Text(stringResource(id = android.R.string.ok))
                }
            }
        )
    }
}

@Composable
fun DeletionConfirmDialog(showDialog: Boolean,
                          onCancel: () -> Unit,
                          onConfirmed: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
            },

            title = {
                Text(text = stringResource(id = R.string.label_delete))
            },
            text = {
                Text(text = stringResource(id = R.string.prompt_deletion))

            },
            dismissButton = {
                TextButton(onClick = {
                    onCancel()
                }) {
                    Text(stringResource(id = android.R.string.cancel))
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    onConfirmed()
                }) {
                    Text(stringResource(id = android.R.string.ok))
                }
            }
        )
    }
}
