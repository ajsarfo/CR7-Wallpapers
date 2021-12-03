package com.sarftec.cristianoronaldo.domain.repository

import android.graphics.Bitmap
import com.sarftec.cristianoronaldo.domain.model.ImageInfo
import com.sarftec.cristianoronaldo.utils.Resource
import java.io.File

interface ImageRepository {
    suspend fun uploadWallpaper(file: File, imageInfo: ImageInfo): Resource<Unit>

    /*
    * Note => GetImage and deleteImage is irrespective of folder name
     */
    suspend fun getImage(imageName: String): Resource<Bitmap>
    suspend fun deleteImage(imageName: String) : Resource<Unit>
}