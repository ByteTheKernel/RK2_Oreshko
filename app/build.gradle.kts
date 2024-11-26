plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp) // Для Room
}

android {
    namespace = "com.example.rk_2"
    compileSdk = 34

    // Чтение API ключа из local.properties
    val giphyApiKey = project.findProperty("GIPHY_API_KEY") ?: ""

    defaultConfig {
        applicationId = "com.example.rk_2"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            buildConfigField("String", "GIPHY_API_KEY", "\"$giphyApiKey\"")
        }
        release {
            buildConfigField("String", "GIPHY_API_KEY", "\"$giphyApiKey\"")
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    // Добавление ключа в BuildConfig
    buildTypes.forEach {
        it.buildConfigField("String", "GIPHY_API_KEY", "\"$giphyApiKey\"")
    }
}

dependencies {
    // Основные зависимости Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.material)

    // Jetpack Compose
    implementation(libs.compose.ui)
    implementation(libs.compose.material)
    implementation(libs.accompanist.flowlayout)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation (libs.accompanist.flowlayout.v0313beta)

    // Coil (для загрузки изображений)
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)

    // Retrofit (для API)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.logging)

    // Room (для локального кэша)
    implementation(libs.room.runtime)
    implementation(libs.androidx.ui.tooling.preview.android)
    ksp(libs.room.compiler)

    // Coroutines (для асинхронной обработки)
    implementation(libs.coroutines)

    // Тестирование
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}