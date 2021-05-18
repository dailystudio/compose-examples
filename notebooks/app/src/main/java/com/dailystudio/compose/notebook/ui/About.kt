package com.dailystudio.compose.notebook.ui

import android.view.LayoutInflater
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.dailystudio.compose.notebook.R
import com.dailystudio.compose.notebook.theme.NotesTheme


@Composable
fun AboutDialog(showDialog: Boolean,
                onClose: () -> Unit
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = {
            },
        ) {
            Column(
                Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colors.surface)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_thumb),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.height(200.dp).fillMaxWidth()
                )
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AndroidView(
                        factory = {
                            val view = LayoutInflater.from(it).inflate(
                                R.layout.layout_launcher_icon, null, false)
                            view
                        },
                        modifier = Modifier
                            .size(72.dp)
                    )
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(text = stringResource(id = R.string.app_name),
                            style = MaterialTheme.typography.h5
                        )
                        Text(text = "1.0.0",
                            style = MaterialTheme.typography.body2
                        )

                    }
                }
                AndroidView(
                    factory = {
                        val view = LayoutInflater.from(it).inflate(
                            R.layout.layout_description_text, null, false)
                        view
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    TextButton(onClick = {
                        onClose()
                    }) {
                        Text(stringResource(id = android.R.string.ok))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AboutDialogPreview() {
    NotesTheme() {
        AboutDialog(showDialog = true) {
            
        }
    }
}