package com.sarftec.cristianoronaldo.view.adapter.viewholder.wallpaper

import android.view.ViewGroup
import com.sarftec.cristianoronaldo.databinding.LayoutWallpaperItemBinding

class FavoriteWallpaperViewHolder(
    layoutBinding: LayoutWallpaperItemBinding,
    dependency: ViewHolderDependency
) : BaseWallpaperItemViewHolder(layoutBinding, dependency) {

    override fun showBottomLayout(): Boolean = false

    companion object {
        fun getInstance(
            parent: ViewGroup,
            dependency: ViewHolderDependency
        ): FavoriteWallpaperViewHolder {
            return FavoriteWallpaperViewHolder(getLayoutBinding(parent), dependency)
        }
    }
}