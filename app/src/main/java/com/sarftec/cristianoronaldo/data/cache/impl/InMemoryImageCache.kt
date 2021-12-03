package com.sarftec.cristianoronaldo.data.cache.impl

import android.graphics.Bitmap
import android.util.LruCache
import com.sarftec.cristianoronaldo.data.cache.ImageCache
import com.sarftec.cristianoronaldo.utils.Resource
import javax.inject.Inject

class InMemoryImageCache @Inject constructor(): ImageCache {

    private var cacheSize = ((Runtime.getRuntime().maxMemory() / 1024).toInt()) / 8

    private val imageCache = createLruCache(cacheSize)

    override suspend fun getImage(id: String): Resource<Bitmap> {
        return imageCache.get(id)?.let { Resource.success(it) }
            ?: Resource.error("Image not in memory!")
    }

    override suspend fun deleteImage(id: String): Resource<Unit> {
        imageCache.remove(id)
        return Resource.success(Unit)
    }

    override suspend fun insertImage(id: String, bitmap: Bitmap) {
        imageCache.put(id, bitmap)
    }

    private fun createLruCache(cacheSize: Int): LruCache<String, Bitmap> {
        return LruImageCache(cacheSize)
    }

    private class LruImageCache(cacheSize: Int) : LruCache<String, Bitmap>(cacheSize) {
        override fun sizeOf(key: String?, value: Bitmap?): Int {
            return value?.byteCount ?: 0
        }
    }
}