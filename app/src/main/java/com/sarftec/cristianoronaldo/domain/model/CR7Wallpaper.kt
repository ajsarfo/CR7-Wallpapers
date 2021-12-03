package com.sarftec.cristianoronaldo.domain.model

data class CR7Wallpaper(
    val id: Long,
    val likes: Long,
    val views: Long,
    val imageLocation: String,
    val section: String,
    val category: String,
    var isFavorite: Boolean = false
)