package com.sarftec.cristianoronaldo.data.cache

import android.net.Uri
import com.sarftec.cristianoronaldo.utils.Resource

interface UriCache {
    suspend fun getUri(id: String) : Resource<Uri>
    suspend fun setUri(id: String, uri: Uri)
}