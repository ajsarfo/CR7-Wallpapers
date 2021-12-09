package com.sarftec.cristianoronaldo.data.cache

import android.net.Uri
import com.sarftec.cristianoronaldo.data.room.CR7Database
import com.sarftec.cristianoronaldo.data.room.entity.RoomImage
import com.sarftec.cristianoronaldo.utils.Resource
import javax.inject.Inject

class UriCacheImpl @Inject constructor(
    private val appDatabase: CR7Database
) : UriCache {

    override suspend fun getUri(id: String): Resource<Uri> {
        return appDatabase.roomImageDao().getImage(id)
            ?.let { Resource.success(it.uri) }
            ?: Resource.error("Error => Image \'$id\' not found in database!")
    }

    override suspend fun setUri(id: String, uri: Uri) {
        appDatabase.roomImageDao().insertImage(
            RoomImage(id, uri)
        )
    }
}