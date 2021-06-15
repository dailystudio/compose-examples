package com.dailystudio.compose.gallery.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import coil.size.Scale
import com.dailystudio.compose.gallery.db.PhotoItem
import com.google.accompanist.coil.rememberCoilPainter

@ExperimentalFoundationApi
@Composable
fun Photos(photoItems: LazyPagingItems<PhotoItem>) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(2)
    ) {
        items(photoItems.itemCount) { index ->
            val p = photoItems.getAsState(index = index).value ?: return@items

            Card(modifier = Modifier
                .height(250.dp)
                .padding(8.dp),
                elevation = 3.dp,
                shape = MaterialTheme.shapes.medium.copy(
                    CornerSize(5.dp)
                )
            ) {
                Surface(color = Color.Black) {
                    Box(modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        Image(
                            painter = rememberCoilPainter(p.thumbnailUrl),
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                        )
                        Text(
                            text = "by ${p.author}",
                            style = MaterialTheme.typography.body2.copy(
                                color = Color.White
                            ),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                }
            }
        }
        
    }
}