package com.sarftec.cristianoronaldo.data.downloader

import android.graphics.Bitmap
import android.net.Uri
import com.sarftec.cristianoronaldo.utils.Resource

interface ImageDownloader {
    suspend fun downloadImage(uri: Uri) : Resource<Bitmap>
}