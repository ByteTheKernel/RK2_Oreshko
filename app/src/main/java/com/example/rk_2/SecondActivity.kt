package com.example.rk_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.example.rk_2.repository.GiphyRepository
import com.example.rk_2.ui.OneGif
import com.example.rk_2.ui.GifScreen
import com.example.rk_2.ui.theme.MyAppTheme

class SecondActivity : ComponentActivity()  {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        val url = intent.getStringExtra("URL")

        setContent {
            // Устанавливаем Compose-контент
            MyAppTheme {
                OneGif(url.toString())
            }
        }
    }
}