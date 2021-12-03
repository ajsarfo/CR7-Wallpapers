
package com.sarftec.cristianoronaldo.view.adapter.viewholder.others

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sarftec.cristianoronaldo.R
import com.sarftec.cristianoronaldo.databinding.LayoutApproveItemBinding
import com.sarftec.cristianoronaldo.utils.Resource
import com.sarftec.cristianoronaldo.view.model.WallpaperUI
import com.sarftec.cristianoronaldo.view.task.Task
import com.sarftec.cristianoronaldo.view.task.TaskManager
import com.sarftec.cristianoronaldo.view.viewmodel.ApproveViewModel
import kotlinx.coroutines.CoroutineScope
import java.util.*

class ApproveItemViewHolder(
    private val layoutBinding: LayoutApproveItemBinding,
    private val dependency: ViewHolderDependency
) : RecyclerView.ViewHolder(layoutBinding.root) {

    val id = UUID.randomUUID().toString()

    var wallpaper: WallpaperUI.Wallpaper? = null

    init {
        layoutBinding.wallpaperCard.setOnClickListener {
            wallpaper?.let { dependency.viewModel.setSelectionState(it) }
        }
    }

    private fun setLayout(wallpaper: WallpaperUI.Wallpaper, image: Resource<Bitmap>) {
        if (image.isError()) {
            Log.v("TAG", "View holder error => ${image.message}")
            dependency.taskManager.removeTask(id)
            return
        }
        layoutBinding.image.setImageBitmap(image.data!!)
        dependency.viewModel.getStateForWallpaper(wallpaper).let { state ->
            updateState(state)
        }
        dependency.taskManager.removeTask(id)
    }

    private fun clearLayout(wallpaper: WallpaperUI.Wallpaper) {
        layoutBinding.selectState.visibility = View.GONE
        layoutBinding.approveState.visibility = View.GONE
        layoutBinding.wallpaperCard.setOnClickListener {

        }
    }

    fun updateState(state: ApproveViewModel.WallpaperState) {
        when (state) {
            ApproveViewModel.WallpaperState.APPROVED -> layoutBinding.apply {
                approveState.setImageResource(R.drawable.ic_check)
                approveState.visibility = View.VISIBLE
                selectState.visibility = View.GONE
            }
            ApproveViewModel.WallpaperState.SELECTED -> layoutBinding.apply {
                selectState.visibility = View.VISIBLE
                approveState.visibility = View.GONE
            }

            ApproveViewModel.WallpaperState.DELETED -> layoutBinding.apply {
                approveState.setImageResource(R.drawable.ic_close)
                approveState.visibility = View.VISIBLE
                selectState.visibility = View.GONE
            }
            else -> layoutBinding.apply {
                approveState.visibility = View.GONE
                selectState.visibility = View.GONE
            }
        }
    }

    fun bind(wallpaper: WallpaperUI) {
        if (wallpaper !is WallpaperUI.Wallpaper) return
        Log.v("TAG", "wallpaper => ${wallpaper.wallpaper.id}")
        this.wallpaper = wallpaper
        clearLayout(wallpaper)
        val task = Task.createTask<WallpaperUI.Wallpaper, Resource<Bitmap>>(
            dependency.coroutineScope,
            wallpaper
        )
        task.addExecution { input -> dependency.viewModel.getImage(input) }
        task.addCallback { setLayout(wallpaper, it) }
        dependency.taskManager.addTask(id, task.build())
    }

    companion object {
        fun getInstance(
            parent: ViewGroup,
            dependency: ViewHolderDependency
        ): ApproveItemViewHolder {
            val binding = LayoutApproveItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ApproveItemViewHolder(binding, dependency)
        }
    }

    class ViewHolderDependency(
        val coroutineScope: CoroutineScope,
        val taskManager: TaskManager<WallpaperUI.Wallpaper, Resource<Bitmap>>,
        val viewModel: ApproveViewModel
    )
}