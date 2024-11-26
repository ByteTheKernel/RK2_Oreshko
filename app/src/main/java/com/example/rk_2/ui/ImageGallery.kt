package com.example.rk_2.ui

import android.content.Intent
import android.util.Log
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.rk_2.viewModel.GiphyViewModel
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.decode.GifDecoder
import coil.request.ImageRequest
import coil.size.Scale
import com.example.rk_2.R
import com.example.rk_2.SecondActivity


@Composable
fun GifCard(gif: GifData) {
    val context = LocalContext.current
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(gif.images.original.url)
            .crossfade(true) // Для плавной загрузки
            .decoderFactory(GifDecoder.Factory()) // Указывает использовать декодер для GIF
            .build()
    )

    Box(
        modifier = Modifier
            .clickable {

                val intent = Intent(context, SecondActivity::class.java)
                intent.putExtra("URL", gif.images.original.url )
                context.startActivity(intent)
            }
            .padding(8.dp)
            .fillMaxWidth()
            .aspectRatio(
                if (gif.images.original.height > 0)
                    gif.images.original.width.toFloat() / gif.images.original.height
                else 1f)
            .background(Color.Gray, shape = RoundedCornerShape(8.dp))
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Показываем индикатор загрузки, если изображение все еще загружается
        if (painter.state is AsyncImagePainter.State.Loading) {
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

/*@Composable
fun GifScreen(viewModel: GiphyViewModel = viewModel()) {
    val gifs by viewModel.gifs.collectAsState()

    /*GifGrid(
        gifs = gifs,
        onLoadMore = { viewModel.loadMoreImages() }
    )*/
    MasonryGrid(
        gifs = gifs,
        columns = 2, // Указываем нужное количество столбцов
        onLoadMore = { viewModel.loadMoreImages() }
    )
}*/

@Composable
fun GifScreen(viewModel: GiphyViewModel = viewModel()) {
    val gifs by viewModel.gifs.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    if (errorMessage != null) {
        // Заглушка для всей страницы
        FullScreenError(
            message = errorMessage ?: "Произошла ошибка",
            onRetry = { viewModel.retryLoading() }
        )
    } else {
        // Основной контент
        MasonryGrid(
            gifs = gifs,
            columns = 2,
            isLoading = isLoading,
            onLoadMore = { viewModel.loadMoreImages() },
            onRetryLoadMore = { viewModel.retryLoadingMore() }
        )
    }
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

@Composable
fun MasonryGrid(
    gifs: List<GifData>,
    columns: Int = 3, // Количество столбцов
    contentPadding: PaddingValues = PaddingValues(8.dp),
    horizontalSpacing: Dp = 8.dp,
    verticalSpacing: Dp = 8.dp,
    isLoading: Boolean, // Добавляем параметр для отображения индикатора загрузки
    onLoadMore: () -> Unit, // Добавляем параметр для загрузки больше контента
    onRetryLoadMore: () -> Unit // Параметр для повторной попытки загрузки
) {
    LazyColumn(
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(verticalSpacing),
        modifier = Modifier.fillMaxSize()
    ) {
        val columnHeights = IntArray(columns) { 0 }
        val itemsInColumns = List(columns) { mutableListOf<GifData>() }

        // Распределяем GIF-ы по столбцам с учетом их высоты
        gifs.forEach { gif ->
            val shortestColumn = columnHeights.indexOf(columnHeights.minOrNull() ?: 0)
            itemsInColumns[shortestColumn].add(gif)
            columnHeights[shortestColumn] += gif.images.original.height
        }

        // Добавляем строки в LazyColumn
        val maxRows = itemsInColumns.maxOf { it.size }
        for (rowIndex in 0 until maxRows) {
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(horizontalSpacing),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (columnIndex in 0 until columns) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = if (columnIndex > 0) horizontalSpacing else 0.dp)
                        ) {
                            itemsInColumns.getOrNull(columnIndex)?.getOrNull(rowIndex)?.let { gif ->
                                GifCard(gif)
                            }
                        }
                    }
                }
            }
        }

        // Индикатор загрузки в конце
        if (isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(32.dp)
                            .align(Alignment.Center)
                    )
                }
            }
        }

        // Плашка для ошибки в пагинации
        if (!isLoading && gifs.isNotEmpty()) {
            item {
                PaginationErrorItem(onRetry = onRetryLoadMore)
            }
        }
    }

    // Загрузка при необходимости
    if (gifs.isNotEmpty() && !isLoading) {
        onLoadMore()
    }
}

@Composable
fun FullScreenError(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.error,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Button(onClick = onRetry) {
                Text("Повторить попытку")
            }
        }
    }
}

@Composable
fun PaginationErrorItem(
    message: String = "Не удалось загрузить данные",
    onRetry: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.error,
            modifier = Modifier.padding(end = 8.dp)
        )
        Button(onClick = onRetry) {
            Text("Повторить")
        }
    }
}

@Composable
fun OneGif(gif_url: String){
    val context = LocalContext.current
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(gif_url)
            .decoderFactory(GifDecoder.Factory()) // Указывает использовать декодер для GIF
            .build()
    )
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }

}





