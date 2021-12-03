package com.sarftec.cristianoronaldo.view.fragment_wallpaper

import androidx.lifecycle.lifecycleScope
import com.sarftec.cristianoronaldo.data.room.CR7RoomDatabase
import com.sarftec.cristianoronaldo.view.adapter.WallpaperFavoriteAdapter
import com.sarftec.cristianoronaldo.view.utils.toast
import com.sarftec.cristianoronaldo.view.viewmodel.WallpapersViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteWallpaperFragment : BaseWallpaperFragment() {

    override fun getSelectionType(): WallpapersViewModel.Selection {
        return WallpapersViewModel.Selection.FAVORITE
    }

    override val wallpaperAdapter by lazy {
        WallpaperFavoriteAdapter(lifecycleScope, viewModel) {
            wallpaperFragmentListener?.navigateOtherToDetail(it, getSelectionType())
        }
    }

    @Inject
    lateinit var appDatabase: CR7RoomDatabase
}