package com.sarftec.cristianoronaldo.view.adapter

import android.graphics.Bitmap
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.sarftec.cristianoronaldo.utils.Resource
import com.sarftec.cristianoronaldo.view.adapter.diffutil.WallpaperItemDiffUtil
import com.sarftec.cristianoronaldo.view.adapter.viewholder.others.ApproveItemViewHolder
import com.sarftec.cristianoronaldo.view.model.WallpaperUI
import com.sarftec.cristianoronaldo.view.task.TaskManager
import com.sarftec.cristianoronaldo.view.viewmodel.ApproveViewModel
import kotlinx.coroutines.CoroutineScope

class ApproveAdapter(
    coroutineScope: CoroutineScope,
    private val viewModel: ApproveViewModel
) : PagingDataAdapter<WallpaperUI, ApproveItemViewHolder>(WallpaperItemDiffUtil) {

    private val taskManager = TaskManager<WallpaperUI.Wallpaper, Resource<Bitmap>>()

    private val dependency = ApproveItemViewHolder.ViewHolderDependency(
        coroutineScope,
        taskManager,
        viewModel
    )

    private val holderSet = hashSetOf<ApproveItemViewHolder>()

    override fun onBindViewHolder(holder: ApproveItemViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
        holderSet.add(holder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApproveItemViewHolder {
        return ApproveItemViewHolder.getInstance(parent, dependency)
    }

    fun updateViewHolder(state: ApproveViewModel.AdapterUpdate) {
        when (state) {
            is ApproveViewModel.AdapterUpdate.Single -> {
                val viewHolder = holderSet.firstOrNull {
                    val id = it.wallpaper?.wallpaper?.id
                    id != null && id.toString() == state.id
                }
                viewHolder?.updateState(state.state)
            }
            else -> holderSet.forEach { viewHolder ->
               viewHolder.wallpaper?.let {
                   viewHolder.updateState(viewModel.getStateForWallpaper(it))
               }
            }
        }
    }
}