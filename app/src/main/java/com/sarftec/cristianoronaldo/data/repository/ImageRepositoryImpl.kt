package com.sarftec.cristianoronaldo.data.repository

import android.graphics.Bitmap
import com.sarftec.cristianoronaldo.data.cache.DiskImageCacheQualifier
import com.sarftec.cristianoronaldo.data.cache.ImageCache
import com.sarftec.cristianoronaldo.data.cache.InMemoryImageCacheQualifier
import com.sarftec.cristianoronaldo.data.downloader.ImageDownloader
import com.sarftec.cristianoronaldo.data.firebase.repository.FirebaseImageRepository
import com.sarftec.cristianoronaldo.domain.model.ImageInfo
import com.sarftec.cristianoronaldo.domain.repository.ImageRepository
import com.sarftec.cristianoronaldo.utils.Resource
import java.io.File
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val firebaseImageRepository: FirebaseImageRepository,
    @DiskImageCacheQualifier private val diskImageCache: ImageCache,
    @InMemoryImageCacheQualifier private val inMemoryImageCache: ImageCache,
    private val imageDownloader: ImageDownloader
) : ImageRepository {

    override suspend fun getImage(imageName: String): Resource<Bitmap> {
        inMemoryImageCache.getImage(imageName).let {
            if(it.isSuccess()) return it
        }

        diskImageCache.getImage(imageName).let {
            if(it.isSuccess()) {
                inMemoryImageCache.insertImage(imageName, it.data!!)
                return it
            }
        }

        firebaseImageRepository.getImageUri(imageName).let {
            if(it.isSuccess()) {
                val result = imageDownloader.downloadImage(it.data!!)
                if(result.isSuccess()) {
                    diskImageCache.insertImage(imageName, result.data!!)
                    inMemoryImageCache.insertImage(imageName, result.data)
                }
                return result
            }
            if(it.isError()) return Resource.error("${it.message}")
        }
        return Resource.error("Failed => all image retrieval methods failed!")
    }

    override suspend fun uploadWallpaper(file: File, imageInfo: ImageInfo): Resource<Unit> {
        return firebaseImageRepository.uploadWallpaper(file, imageInfo.toFullName())
    }

    override suspend fun deleteImage(imageName: String): Resource<Unit> {
        firebaseImageRepository.deleteImage(imageName)
        return Resource.success(Unit)
    }
}