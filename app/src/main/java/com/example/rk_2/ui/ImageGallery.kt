package com.example.rk_2.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import com.google.accompanist.flowlayout.FlowRow
import com.example.rk_2.data.model.GifData
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.rk_2.viewModel.GiphyViewModel
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter
import com.example.rk_2.R


@Composable
/*fun GifCard(gif: GifData) {
    val validAspectRatio = if (gif.images.original.height > 0) {
        gif.images.original.width.toFloat() / gif.images.original.height
    } else 1f

    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .aspectRatio(validAspectRatio)
            .background(Color.Gray, shape = RoundedCornerShape(8.dp))
    ) {
        AsyncImage(
            model = gif.images.original.url,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            loading = {
                CircularProgressIndicator(modifier = Modifier.fillMaxSize())
            }
        )
    }
    Log.d("GifCard", "GIF URL: ${gif.images.original.url}")
}*/
fun GifCard(gif: GifData) {
    val painter = rememberAsyncImagePainter(gif.images.original.url)

    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .aspectRatio(if (gif.images.original.height > 0) gif.images.original.width.toFloat() / gif.images.original.height else 1f)
            .background(Color.Gray, shape = RoundedCornerShape(8.dp))
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Показываем индикатор загрузки, если изображение все еще загружается
        if (painter.state is coil.compose.AsyncImagePainter.State.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun GifGrid(
    gifs: List<GifData>,
    onLoadMore: () -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(gifs) { index, gif ->
            GifCard(gif)

            if (index >= gifs.size - 5) {
                onLoadMore()
            }
        }

        item {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }
    }
}

@Composable
fun GifScreen(viewModel: GiphyViewModel = viewModel()) {
    val gifs by viewModel.gifs.collectAsState()

    GifGrid(
        gifs = gifs,
        onLoadMore = { viewModel.loadMoreImages() }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GifFlowGrid(gifs: List<GifData>, onLoadMore: () -> Unit) {
    FlowRow(
        mainAxisSpacing = 8.dp,
        crossAxisSpacing = 8.dp,
        modifier = Modifier.padding(8.dp)
    ) {
        gifs.forEach { gif ->
            GifCard(gif)
        }
    }
}

