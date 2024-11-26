package com.example.rk_2.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.rk_2.data.model.GifData
import com.example.rk_2.data.network.RetrofitClient
import com.example.rk_2.repository.GiphyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class GiphyViewModelFactory(
    private val repository: GiphyRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GiphyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GiphyViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class GiphyViewModel(private val repository: GiphyRepository) : ViewModel() {
    private val _gifs = MutableStateFlow<List<GifData>>(emptyList())
    val gifs: StateFlow<List<GifData>> get() = _gifs

    private var currentPage = 0
    var isLoading = false

    // Новый метод для поиска по запросу
    fun searchGifs(query: String) {
        if (isLoading) return

        isLoading = true
        currentPage = 0  // Сбрасываем страницу для нового запроса
        viewModelScope.launch {
            try {
                Log.d("GiphyViewModel", "Request URL: ${RetrofitClient.BASE_URL}v1/gifs/search")
                // Используем асинхронную версию без execute()
                val response = repository.searchGifs(query, 20, currentPage * 20)

                if (response.isSuccessful) {
                    val newGifs = response.body()?.data ?: emptyList()
                    _gifs.value = newGifs  // Очищаем и загружаем новый список
                    currentPage++
                    Log.d("GiphyViewModel", "GIFs loaded successfully")
                } else {
                    Log.e("GiphyViewModel", "Response error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("GiphyViewModel", "Error during API request: ${e.message}", e)
            } finally {
                isLoading = false
            }
        }
    }


    fun loadMoreImages() {
        if (isLoading) return

        isLoading = true
        viewModelScope.launch {
            try {
                val response = repository.searchGifs("nba", 20, currentPage * 20) // Используем правильный запрос
                if (response.isSuccessful) {
                    val newGifs = response.body()?.data ?: emptyList()
                    _gifs.value += newGifs  // Добавляем новые GIF в список
                    currentPage++
                }
            } catch (e: Exception) {
                Log.e("GiphyViewModel", "Error during API request: ${e.message}", e)
            } finally {
                isLoading = false
            }
        }
    }

}

