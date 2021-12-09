package com.sarftec.cristianoronaldo.view.adapter

import android.net.Uri
import androidx.paging.PagingDataAdapter
import com.sarftec.cristianoronaldo.utils.Resource
import com.sarftec.cristianoronaldo.view.adapter.diffutil.WallpaperItemDiffUtil
import com.sarftec.cristianoronaldo.view.adapter.viewholder.wallpaper.BaseViewHolder
import com.sarftec.cristianoronaldo.view.adapter.viewholder.wallpaper.BaseWallpaperItemViewHolder
import com.sarftec.cristianoronaldo.view.model.WallpaperUI
import com.sarftec.cristianoronaldo.view.task.TaskManager
import com.sarftec.cristianoronaldo.view.viewmodel.WallpapersViewModel
import kotlinx.coroutines.CoroutineScope

abstract class BaseWallpaperItemAdapter(
    coroutineScope: CoroutineScope,
    viewModel: WallpapersViewModel,
    onClick: (WallpaperUI.Wallpaper) -> Unit
) : PagingDataAdapter<WallpaperUI, BaseViewHolder>(WallpaperItemDiffUtil) {

    private val taskManager = TaskManager<WallpaperUI.Wallpaper, Resource<Uri>>()

    protected val dependency = BaseWallpaperItemViewHolder.ViewHolderDependency(
        viewModel,
        coroutineScope,
        taskManager,
        onClick
    )

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position)?.viewType ?: -1
    }
}