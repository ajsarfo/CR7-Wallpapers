package com.sarftec.cristianoronaldo.view.adapter.viewholder.wallpaper

import android.view.ViewGroup
import com.sarftec.cristianoronaldo.databinding.LayoutWallpaperItemBinding

class WallpaperItemViewHolder(
    layoutBinding: LayoutWallpaperItemBinding,
    dependency: ViewHolderDependency
) : BaseWallpaperItemViewHolder(layoutBinding, dependency) {

    override fun showBottomLayout(): Boolean = true

    companion object {
        fun getInstance(
            parent: ViewGroup,
            dependency: ViewHolderDependency
        ): WallpaperItemViewHolder {
            return WallpaperItemViewHolder(getLayoutBinding(parent), dependency)
        }
    }
}