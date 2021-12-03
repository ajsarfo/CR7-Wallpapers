package com.sarftec.cristianoronaldo.data.firebase.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sarftec.cristianoronaldo.data.firebase.extra.FirebaseKey
import com.sarftec.cristianoronaldo.data.firebase.repository.walllpaper.FirebaseBaseRepository
import com.sarftec.cristianoronaldo.data.firebase.repository.walllpaper.FirebaseBaseWallpaperRepository
import com.sarftec.cristianoronaldo.domain.model.CR7Wallpaper

class FirebaseWallpaperPagingSource(
    private val repository: FirebaseBaseRepository,
    private val startId: Long = INITIAL_ID
) : PagingSource<FirebaseKey, CR7Wallpaper>() {

    override fun getRefreshKey(state: PagingState<FirebaseKey, CR7Wallpaper>): FirebaseKey? {
        return null
    }

    override suspend fun load(params: LoadParams<FirebaseKey>): LoadResult<FirebaseKey, CR7Wallpaper> {
        return try {
            val nextKey = params.key ?: return getFirstPage(startId)
           val result = repository.getWallpapers(nextKey)

            LoadResult.Page(
                data = result.data,
                nextKey = result.nextKey,
                prevKey = result.previousKey
            )
        } catch (e: Exception) {
            Log.v("TAG", "${e.message}")
            LoadResult.Error(e)
        }
    }

    private suspend fun getFirstPage(id: Long): LoadResult<FirebaseKey, CR7Wallpaper> {
        return try {
            val key = if (isInitialId(id)) FirebaseKey.getInitialKey()
            else FirebaseKey.ID(FirebaseKey.Direction.NEXT, id)

            val result = repository.loadFirstPage(key)
            LoadResult.Page(
                data = result.data,
                nextKey = result.nextKey,
                prevKey = result.previousKey
            )
        } catch (e: Exception) {
            Log.v("TAG", "${e.message}")
            LoadResult.Error(e)
        }
    }

    companion object {
        private const val INITIAL_ID = -1L
        fun isInitialId(id: Long): Boolean = INITIAL_ID == id
    }
}