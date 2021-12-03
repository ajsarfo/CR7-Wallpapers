package com.sarftec.cristianoronaldo.view.model

import com.sarftec.cristianoronaldo.domain.model.CR7Wallpaper

sealed class WallpaperUI(val viewType: Int) {
    class Wallpaper(
        val wallpaper: CR7Wallpaper
    ) : WallpaperUI(UI_MODEL)

    companion object {
        const val UI_MODEL = 0
        const val UI_AD = 1
    }
}