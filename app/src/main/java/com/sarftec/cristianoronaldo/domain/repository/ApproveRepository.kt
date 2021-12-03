package com.sarftec.cristianoronaldo.domain.repository

import androidx.paging.PagingData
import com.sarftec.cristianoronaldo.domain.model.CR7Wallpaper
import com.sarftec.cristianoronaldo.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ApproveRepository {
    suspend fun getWallpapers() : Resource<Flow<PagingData<CR7Wallpaper>>>
    suspend fun approveWallpaper(wallpaper: CR7Wallpaper) : Resource<Unit>
    suspend fun deleteWallpaper(wallpaper: CR7Wallpaper) : Resource<Unit>
}