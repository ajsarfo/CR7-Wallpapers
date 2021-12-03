package com.sarftec.cristianoronaldo.view.fragment_wallpaper

import androidx.lifecycle.lifecycleScope
import com.sarftec.cristianoronaldo.view.adapter.WallpaperItemAdapter
import com.sarftec.cristianoronaldo.view.viewmodel.WallpapersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PopularWallpaperFragment : BaseWallpaperFragment() {

    override val wallpaperAdapter by lazy {
        WallpaperItemAdapter(lifecycleScope, viewModel) {
            wallpaperFragmentListener?.navigateOtherToDetail(it, getSelectionType())
        }
    }

    override fun getSelectionType(): WallpapersViewModel.Selection {
        return WallpapersViewModel.Selection.POPULAR
    }
}