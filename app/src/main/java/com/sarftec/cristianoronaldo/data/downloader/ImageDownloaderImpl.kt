package com.sarftec.cristianoronaldo.data.downloader

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener
import com.sarftec.cristianoronaldo.utils.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CompletableDeferred
import javax.inject.Inject

class ImageDownloaderImpl @Inject constructor(
  @ApplicationContext private val context: Context
) : ImageDownloader {

    private val imageLoader = ImageLoader.getInstance()

    init {
        imageLoader.init(ImageLoaderConfiguration.createDefault(context))
    }

    override suspend fun downloadImage(uri: Uri): Resource<Bitmap> {
        var resource: Resource<Bitmap> = Resource.error("Error => Should not happen!")
        loadImage(uri) { resource = it }
        return resource
    }

    private suspend fun loadImage(image: Uri, callback: (Resource<Bitmap>) -> Unit) {
        val result = CompletableDeferred<Resource<Bitmap>>(null)
        imageLoader.loadImage(
            image.toString(),
            object : ImageLoadingListener {
                override fun onLoadingStarted(imageUri: String?, view: View?) {

                }

                override fun onLoadingFailed(
                    imageUri: String?,
                    view: View?,
                    failReason: FailReason?
                ) {
                    result.complete(
                        Resource.error("Failed => could not download image!")
                    )
                }

                override fun onLoadingComplete(
                    imageUri: String?,
                    view: View?,
                    loadedImage: Bitmap?
                ) {
                    val resource = loadedImage?.let { Resource.success(it) }
                        ?: Resource.error("Failed => image download NULL!")
                    result.complete(resource)
                }

                override fun onLoadingCancelled(imageUri: String?, view: View?) {
                    result.complete(
                        Resource.error("Failed => image download cancelled!")
                    )
                }
            }
        )
        callback(result.await())
    }
}