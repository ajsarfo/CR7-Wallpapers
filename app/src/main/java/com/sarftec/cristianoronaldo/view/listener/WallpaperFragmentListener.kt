package com.sarftec.cristianoronaldo.view.listener

import com.sarftec.cristianoronaldo.view.model.WallpaperUI
import com.sarftec.cristianoronaldo.view.viewmodel.WallpapersViewModel

interface WallpaperFragmentListener {
    fun navigateOtherToDetail(
        wallpaperUI: WallpaperUI.Wallpaper,
        selection: WallpapersViewModel.Selection
    )
}