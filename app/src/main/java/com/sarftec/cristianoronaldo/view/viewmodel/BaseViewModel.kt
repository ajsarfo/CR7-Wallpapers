package com.sarftec.cristianoronaldo.view.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.sarftec.cristianoronaldo.domain.usecase.image.GetImage
import com.sarftec.cristianoronaldo.utils.Resource
import com.sarftec.cristianoronaldo.view.model.WallpaperUI

abstract class BaseViewModel <T> (
    protected val getImage: GetImage
) : ViewModel() {

    private val cacheImageMap = hashMapOf<Int, T>()

    fun setAtPosition(position: Int, wallpaper: T) {
        cacheImageMap[position] = wallpaper
    }

    fun getAtPosition(position: Int): T? {
        return cacheImageMap[position]
    }

    suspend fun getImage(wallpaperUI: WallpaperUI.Wallpaper): Resource<Bitmap> {
        return getImage(wallpaperUI.wallpaper.imageLocation)
    }

    suspend fun getImage(imageLocation: String): Resource<Bitmap> {
        return getImage.execute(
            GetImage.GetImageParam(imageLocation)
        ).image
    }
}