package com.sarftec.cristianoronaldo.data.firebase.paging

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.sarftec.cristianoronaldo.data.DATA_PAGE_SIZE
import com.sarftec.cristianoronaldo.data.firebase.repository.walllpaper.FirebaseBaseWallpaperRepository
import com.sarftec.cristianoronaldo.data.firebase.repository.walllpaper.FirebaseCategoryWallpaperRepository
import com.sarftec.cristianoronaldo.data.room.CR7RoomDatabase
import com.sarftec.cristianoronaldo.data.room.mapper.RoomFavoriteWallpaperMapper
import com.sarftec.cristianoronaldo.domain.model.CR7Wallpaper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebasePagingInteractor @Inject constructor(
    private val appDatabase: CR7RoomDatabase,
    private val roomFavoriteMapper: RoomFavoriteWallpaperMapper
) {

    fun getWallpaperFlowForId(
        id: Long,
        repository: FirebaseBaseWallpaperRepository
    ): Flow<PagingData<CR7Wallpaper>> {
        Log.v("TAG", "Selected item id => $id")
        return Pager(PagingConfig(DATA_PAGE_SIZE.toInt(), enablePlaceholders = false)) {
            FirebaseWallpaperPagingSource(repository, id)
        }.flow
    }

    fun getInitialWallpaperFlow(
        repository: FirebaseBaseWallpaperRepository
    ): Flow<PagingData<CR7Wallpaper>> {
        return Pager(PagingConfig(DATA_PAGE_SIZE.toInt(), enablePlaceholders = false)) {
            FirebaseWallpaperPagingSource(repository)
        }.flow
    }

    fun getCategoryWallpaperFlow(
        categoryId: String
    ) : Flow<PagingData<CR7Wallpaper>> {
        return Pager(PagingConfig(DATA_PAGE_SIZE.toInt(), enablePlaceholders = false)) {
            FirebaseWallpaperPagingSource(
                FirebaseCategoryWallpaperRepository(categoryId)
            )
        }.flow
    }

    fun getFavoriteWallpaperFlow() : Flow<PagingData<CR7Wallpaper>> {
       return Pager(PagingConfig(DATA_PAGE_SIZE.toInt(), enablePlaceholders = false)) {
            appDatabase.roomFavoriteWallpaperDao().getPagingSource()
        }.flow.map {  pagingData ->
            pagingData.map {
                roomFavoriteMapper.toCR7Wallpaper(it)
            }
       }
    }
}