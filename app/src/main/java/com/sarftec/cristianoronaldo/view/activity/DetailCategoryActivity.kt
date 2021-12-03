package com.sarftec.cristianoronaldo.view.activity

import androidx.activity.viewModels
import com.sarftec.cristianoronaldo.view.parcel.CategoryToDetail
import com.sarftec.cristianoronaldo.view.viewmodel.DetailCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailCategoryActivity : DetailBaseActivity<CategoryToDetail>() {

    override val viewModel by viewModels<DetailCategoryViewModel>()
}