package com.sarftec.cristianoronaldo.view.adapter

import android.view.ViewGroup
import com.sarftec.cristianoronaldo.view.adapter.viewholder.wallpaper.BaseViewHolder
import com.sarftec.cristianoronaldo.view.adapter.viewholder.wallpaper.FavoriteWallpaperViewHolder
import com.sarftec.cristianoronaldo.view.model.WallpaperUI
import com.sarftec.cristianoronaldo.view.viewmodel.WallpapersViewModel
import kotlinx.coroutines.CoroutineScope

class WallpaperFavoriteAdapter(
    coroutineScope: CoroutineScope,
    viewModel: WallpapersViewModel,
    onClick: (WallpaperUI.Wallpaper) -> Unit
) : BaseWallpaperItemAdapter(coroutineScope, viewModel, onClick)  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
       return FavoriteWallpaperViewHolder.getInstance(parent, dependency)
    }
}