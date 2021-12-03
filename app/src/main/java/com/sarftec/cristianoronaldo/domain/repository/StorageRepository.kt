package com.sarftec.cristianoronaldo.domain.repository

import com.sarftec.cristianoronaldo.domain.model.CR7Wallpaper
import com.sarftec.cristianoronaldo.utils.Resource

interface StorageRepository {

    suspend fun saveWallpapers(option: Option, wallpapers: List<CR7Wallpaper>) : Resource<Unit>
    suspend fun saveWallpaper(option: Option, wallpaper: CR7Wallpaper) : Resource<Unit>

    suspend fun removeWallpapers(option: Option) : Resource<Unit>
    suspend fun removeWallpaper(option: Option, wallpaper: CR7Wallpaper) : Resource<Unit>

    enum class Option {
        FAVORITE, NOT_FAVORITE
    }
}