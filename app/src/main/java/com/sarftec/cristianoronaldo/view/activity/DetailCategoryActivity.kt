package com.sarftec.cristianoronaldo.view.activity

import androidx.activity.viewModels
import com.sarftec.cristianoronaldo.R
import com.sarftec.cristianoronaldo.view.parcel.CategoryToDetail
import com.sarftec.cristianoronaldo.view.viewmodel.DetailCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailCategoryActivity : DetailBaseActivity<CategoryToDetail>() {

    override val viewModel by viewModels<DetailCategoryViewModel>()

    override fun bannerId(): Int = R.string.admob_banner_category_detail

    override fun rewardId(): Int = R.string.category_admob_reward_video_id
}