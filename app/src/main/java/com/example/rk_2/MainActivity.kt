package com.example.rk_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.rk_2.repository.GiphyRepository
import com.example.rk_2.ui.GifScreen
import com.example.rk_2.viewModel.GiphyViewModel
import com.example.rk_2.viewModel.GiphyViewModelFactory
import com.example.rk_2.ui.theme.MyAppTheme
import androidx.appcompat.app.AppCompatActivity
import com.example.rk_2.data.network.RetrofitClient


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Создаем репозиторий и ViewModel
        val repository = GiphyRepository()
        val viewModel: GiphyViewModel by viewModels {
            GiphyViewModelFactory(repository)
        }

        // Запускаем поиск GIF по запросу
        viewModel.searchGifs("nba")

        // Устанавливаем Compose-контент
        setContent {
            MyAppTheme {
                GifScreen(viewModel = viewModel)
            }
        }
    }
}