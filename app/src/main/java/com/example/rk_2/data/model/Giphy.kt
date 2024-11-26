package com.example.rk_2.data.model

data class GiphyResponse (
    val data: List<GifData>,
    val pagination: Pagination,
    val meta: Meta
)

/*data class GifData(
    val id: String,
    val url: String,
    val images: Images
)*/

data class GifData(
    val id: String,
    val images: Images
)

data class Images(
    val original: Original
)

data class Original(
    val url: String,
    val width: Int,
    val height: Int
)

data class Pagination(
    val total_count: Int,
    val count: Int,
    val offset: Int
)

data class Meta(
    val status: Int,
    val msg: String
)