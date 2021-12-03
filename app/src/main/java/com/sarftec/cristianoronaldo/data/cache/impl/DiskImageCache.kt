package com.sarftec.cristianoronaldo.data.cache.impl

import android.graphics.Bitmap
import com.sarftec.cristianoronaldo.data.cache.ImageCache
import com.sarftec.cristianoronaldo.data.room.CR7RoomDatabase
import com.sarftec.cristianoronaldo.data.room.entity.RoomImage
import com.sarftec.cristianoronaldo.utils.Resource
import javax.inject.Inject

class DiskImageCache @Inject constructor(
    private val roomDatabase: CR7RoomDatabase
) : ImageCache {

    override suspend fun getImage(id: String): Resource<Bitmap> {
        return roomDatabase.roomImageDao().getRoomImage(id)?.let {
            Resource.success(it.image)
        } ?: Resource.error("Image not in local storage")
    }

    override suspend fun deleteImage(id: String): Resource<Unit> {
        roomDatabase.roomImageDao().deleteRoomImage(id)
        return Resource.success(Unit)
    }

    override suspend fun insertImage(id: String, bitmap: Bitmap) {
        roomDatabase.roomImageDao().insertRoomImage(
            RoomImage(id, bitmap)
        )
    }
}