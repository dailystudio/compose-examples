package com.dailystudio.compose.notebook.ui

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
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
                Text(text = stringResource(id = R.string.dialog_title_new_notebook))
            },
            text = {
                TextField(value = notebookName,
                    onValueChange = {
                        notebookName = it
                    }
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
