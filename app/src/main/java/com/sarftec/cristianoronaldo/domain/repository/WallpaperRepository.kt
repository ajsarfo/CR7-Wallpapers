package com.sarftec.cristianoronaldo.domain.repository

import androidx.paging.PagingData
import com.sarftec.cristianoronaldo.domain.model.CR7Category
import com.sarftec.cristianoronaldo.domain.model.CR7Wallpaper
import com.sarftec.cristianoronaldo.domain.model.ImageInfo
import com.sarftec.cristianoronaldo.utils.Resource
import kotlinx.coroutines.flow.Flow

interface WallpaperRepository {
    suspend fun getWallpapersForSelection(section: RepoSection) : Resource<Flow<PagingData<CR7Wallpaper>>>
    suspend fun getWallpapersForCategory(categoryId: String) : Resource<Flow<PagingData<CR7Wallpaper>>>
    suspend fun getWallpaperCategories() : Resource<List<CR7Category>>
    suspend fun createWallpaper(section: RepoSection, imageInfo: ImageInfo, categoryId: String) : Resource<CR7Wallpaper>
    suspend fun deleteWallpaper(wallpaper: CR7Wallpaper) : Resource<Unit>

    sealed class RepoSection(val id: Long, val name: String) {
        class Recent(id: Long = INITIAL_SECTION) : RepoSection(id, "recent")
        class Popular(id: Long = INITIAL_SECTION) : RepoSection(id, "popular")
        class Favorite(id: Long = INITIAL_SECTION) : RepoSection(id, "favorite")
    }

    companion object {
        const val INITIAL_SECTION = -1L

        fun isInitialSelection(section: RepoSection) : Boolean {
            return section.id == INITIAL_SECTION
        }
    }
}