package com.sarftec.cristianoronaldo.view.adapter

import android.net.Uri
import android.os.Parcelable
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sarftec.cristianoronaldo.utils.Resource
import com.sarftec.cristianoronaldo.view.adapter.diffutil.WallpaperItemDiffUtil
import com.sarftec.cristianoronaldo.view.adapter.viewholder.others.WallpaperDetailViewHolder
import com.sarftec.cristianoronaldo.view.model.WallpaperUI
import com.sarftec.cristianoronaldo.view.task.TaskManager
import com.sarftec.cristianoronaldo.view.viewmodel.DetailBaseViewModel
import kotlinx.coroutines.CoroutineScope

class WallpaperDetailAdapter <T : Parcelable> (
    coroutineScope: CoroutineScope,
    viewModel: DetailBaseViewModel<T>
) : PagingDataAdapter<WallpaperUI, WallpaperDetailViewHolder<T>>(WallpaperItemDiffUtil) {

    private val taskManager = TaskManager<WallpaperUI.Wallpaper, Resource<Uri>>()

    private val dependency = WallpaperDetailViewHolder.ViewHolderDependency(
        viewModel,
        coroutineScope,
        taskManager
    )

    override fun onBindViewHolder(holder: WallpaperDetailViewHolder<T>, position: Int) {
        getItem(position)?.let {
            holder.bind(position, it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallpaperDetailViewHolder<T> {
        return WallpaperDetailViewHolder.getInstance(parent, dependency)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
    }
}