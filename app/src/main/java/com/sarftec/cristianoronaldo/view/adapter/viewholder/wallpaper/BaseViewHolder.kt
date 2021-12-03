package com.sarftec.cristianoronaldo.view.adapter.viewholder.wallpaper

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.sarftec.cristianoronaldo.view.model.WallpaperUI

abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(wallpaperUI: WallpaperUI)
}