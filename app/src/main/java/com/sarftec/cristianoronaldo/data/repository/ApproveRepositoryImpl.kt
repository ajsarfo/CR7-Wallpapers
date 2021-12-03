package com.sarftec.cristianoronaldo.data.repository

import androidx.paging.PagingData
import com.sarftec.cristianoronaldo.data.firebase.mapper.FirebaseWallpaperMapper
import com.sarftec.cristianoronaldo.data.firebase.paging.FirebasePagingInteractor
import com.sarftec.cristianoronaldo.data.firebase.repository.walllpaper.FirebaseApproveWallpaperRepository
import com.sarftec.cristianoronaldo.domain.model.CR7Wallpaper
import com.sarftec.cristianoronaldo.domain.repository.ApproveRepository
import com.sarftec.cristianoronaldo.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ApproveRepositoryImpl @Inject constructor(
    private val pagingInteractor: FirebasePagingInteractor,
    private val firebaseRepository: FirebaseApproveWallpaperRepository,
    private val mapper: FirebaseWallpaperMapper
): ApproveRepository {
    override suspend fun getWallpapers(): Resource<Flow<PagingData<CR7Wallpaper>>> {
        return Resource.success(
            pagingInteractor.getApproveWallpaperFlow()
        )
    }

    override suspend fun approveWallpaper(wallpaper: CR7Wallpaper): Resource<Unit> {
        return withContext(Dispatchers.IO) {
            firebaseRepository.approveWallpaper(
                mapper.toFirebaseWallpaper(wallpaper)
            )
        }
    }

    override suspend fun deleteWallpaper(wallpaper: CR7Wallpaper): Resource<Unit> {
       return withContext(Dispatchers.IO) {
           firebaseRepository.deleteWallpaper(mapper.toFirebaseWallpaper(wallpaper))
       }
    }
}