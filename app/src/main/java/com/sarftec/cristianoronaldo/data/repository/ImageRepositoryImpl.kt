package com.sarftec.cristianoronaldo.data.repository

import android.net.Uri
import com.sarftec.cristianoronaldo.data.cache.UriCache
import com.sarftec.cristianoronaldo.data.firebase.repository.FirebaseImageRepository
import com.sarftec.cristianoronaldo.domain.model.ImageInfo
import com.sarftec.cristianoronaldo.domain.repository.ImageRepository
import com.sarftec.cristianoronaldo.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val repository: FirebaseImageRepository,
    private val uriCache: UriCache
) : ImageRepository {

    override suspend fun getImageUri(imageName: String): Resource<Uri> {
        val resource = uriCache.getUri(imageName)
        if (resource.isSuccess()) return resource
        return withContext(Dispatchers.IO) {
            repository.getImageUri(imageName).also {
                if (it.isSuccess()) uriCache.setUri(imageName, it.data!!)
            }
        }
    }

    override suspend fun uploadWallpaper(file: File, imageInfo: ImageInfo): Resource<Unit> {
        return withContext(Dispatchers.IO) {
            repository.uploadWallpaper(file, imageInfo.toFullName())
        }
    }

    override suspend fun deleteImage(imageName: String): Resource<Unit> {
        repository.deleteImage(imageName)
        return Resource.success(Unit)
    }
}