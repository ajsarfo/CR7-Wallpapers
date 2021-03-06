package com.sarftec.cristianoronaldo.domain.repository

import android.net.Uri
import com.sarftec.cristianoronaldo.domain.model.ImageInfo
import com.sarftec.cristianoronaldo.utils.Resource
import java.io.File

interface ImageRepository {
    suspend fun uploadWallpaper(file: File, imageInfo: ImageInfo): Resource<Unit>

    /*
    * Note => GetImage and deleteImage is irrespective of folder name
     */
    suspend fun getImageUri(imageName: String): Resource<Uri>
    suspend fun deleteImage(imageName: String) : Resource<Unit>
}