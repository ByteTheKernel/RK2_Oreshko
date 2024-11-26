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

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    private var currentPage = 0
    private var isLoadingMore = false

    fun searchGifs(query: String) {
        if (_isLoading.value) return

        _isLoading.value = true
        currentPage = 0  // Сбрасываем страницу для нового запроса
        _errorMessage.value = null  // Сбрасываем предыдущее сообщение об ошибке
        viewModelScope.launch {
            try {
                Log.d("GiphyViewModel", "Request URL: ${RetrofitClient.BASE_URL}v1/gifs/search")
                // Используем асинхронную версию без execute()
                val response = repository.searchGifs(query, 25, currentPage * 20)

                if (response.isSuccessful) {
                    val newGifs = response.body()?.data ?: emptyList()
                    _gifs.value = newGifs  // Очищаем и загружаем новый список
                    currentPage++
                    Log.d("GiphyViewModel", "GIFs loaded successfully")
                } else {
                    Log.e("GiphyViewModel", "Response error: ${response.code()} - ${response.message()}")
                    _errorMessage.value = "Ошибка при загрузке данных"
                }
            } catch (e: Exception) {
                Log.e("GiphyViewModel", "Error during API request: ${e.message}", e)
                _errorMessage.value = "Ошибка соединения или превышен лимит запросов"
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun loadMoreImages() {
        if (_isLoading.value || isLoadingMore) return

        isLoadingMore = true
        _errorMessage.value = null  // Сбрасываем предыдущее сообщение об ошибке

        viewModelScope.launch {
            try {
                val response = repository.searchGifs("nba", 20, currentPage * 20) // Используем правильный запрос
                if (response.isSuccessful) {
                    val newGifs = response.body()?.data ?: emptyList()
                    _gifs.value += newGifs  // Добавляем новые GIF в список
                    currentPage++
                } else {
                    _errorMessage.value = "Ошибка при загрузке дополнительных данных"
                }
            } catch (e: Exception) {
                Log.e("GiphyViewModel", "Error during API request: ${e.message}", e)
                _errorMessage.value = "Ошибка соединения или превышен лимит запросов"
            } finally {
                isLoadingMore = false
            }
        }
    }

    // Функция для повторной попытки загрузки
    fun retryLoading() {
        searchGifs("default") // Например, повторить с запросом по умолчанию
    }

    fun retryLoadingMore() {
        loadMoreImages() // Повторить догрузку данных
    }

}

