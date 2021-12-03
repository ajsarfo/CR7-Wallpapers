package com.sarftec.cristianoronaldo.view.activity

import androidx.activity.viewModels
import com.sarftec.cristianoronaldo.view.parcel.WallpaperToDetail
import com.sarftec.cristianoronaldo.view.viewmodel.DetailWallpaperViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailWallpaperActivity : DetailBaseActivity<WallpaperToDetail>() {
    override val viewModel by viewModels<DetailWallpaperViewModel>()
}