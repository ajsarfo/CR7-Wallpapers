package com.sarftec.cristianoronaldo.view.adapter

import android.view.ViewGroup
import com.sarftec.cristianoronaldo.view.adapter.viewholder.wallpaper.BaseViewHolder
import com.sarftec.cristianoronaldo.view.adapter.viewholder.wallpaper.WallpaperItemViewHolder
import com.sarftec.cristianoronaldo.view.model.WallpaperUI
import com.sarftec.cristianoronaldo.view.viewmodel.WallpapersViewModel
import kotlinx.coroutines.CoroutineScope

class WallpaperItemAdapter(
    coroutineScope: CoroutineScope,
    viewModel: WallpapersViewModel,
    onClick: (WallpaperUI.Wallpaper) -> Unit
) : BaseWallpaperItemAdapter(coroutineScope, viewModel, onClick) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        // if(viewType == ViewUI.UI_MODEL)
        //Implement ad view holder when necessary
        return WallpaperItemViewHolder.getInstance(parent, dependency)
    }
}