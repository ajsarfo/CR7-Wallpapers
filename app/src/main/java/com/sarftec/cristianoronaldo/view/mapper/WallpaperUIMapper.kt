package com.sarftec.cristianoronaldo.view.mapper

import com.sarftec.cristianoronaldo.domain.model.CR7Wallpaper
import com.sarftec.cristianoronaldo.view.model.WallpaperUI
import javax.inject.Inject

class WallpaperUIMapper @Inject constructor(){

    fun mapToViewUI(wallpaper: CR7Wallpaper) : WallpaperUI.Wallpaper {
        return WallpaperUI.Wallpaper(wallpaper)
    }

    fun mapToCR7Wallpaper(wallpaperUI: WallpaperUI.Wallpaper) : CR7Wallpaper {
        return wallpaperUI.wallpaper
    }
}