package com.sarftec.cristianoronaldo.data.cache

import android.graphics.Bitmap
import com.sarftec.cristianoronaldo.utils.Resource

interface ImageCache {
    suspend fun getImage(id: String) : Resource<Bitmap>
    suspend fun deleteImage(id: String) : Resource<Unit>
    suspend fun insertImage(id: String, bitmap: Bitmap)
}