package com.sarftec.cristianoronaldo.view.activity

import androidx.activity.viewModels
import com.sarftec.cristianoronaldo.R
import com.sarftec.cristianoronaldo.view.parcel.WallpaperToDetail
import com.sarftec.cristianoronaldo.view.viewmodel.DetailWallpaperViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailWallpaperActivity : DetailBaseActivity<WallpaperToDetail>() {

    override val viewModel by viewModels<DetailWallpaperViewModel>()

    override fun bannerId(): Int = R.string.admob_banner_wallpaper_detail

    override fun rewardId(): Int = R.string.wallpaper_admob_reward_video_id
}