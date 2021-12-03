package com.sarftec.cristianoronaldo.view.adapter.viewholder.wallpaper

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sarftec.cristianoronaldo.databinding.LayoutWallpaperItemBinding
import com.sarftec.cristianoronaldo.utils.Resource
import com.sarftec.cristianoronaldo.view.model.WallpaperUI
import com.sarftec.cristianoronaldo.view.task.Task
import com.sarftec.cristianoronaldo.view.task.TaskManager
import com.sarftec.cristianoronaldo.view.viewmodel.WallpapersViewModel
import kotlinx.coroutines.CoroutineScope
import java.util.*

abstract class BaseWallpaperItemViewHolder(
    private val layoutBinding: LayoutWallpaperItemBinding,
    private val dependency: ViewHolderDependency
) : BaseViewHolder(layoutBinding.root) {

    private val id = UUID.randomUUID().toString()

    abstract fun showBottomLayout() : Boolean

    private fun setLayout(resource: Resource<Bitmap>) {
        if (resource.isSuccess()) {
            layoutBinding.image.setImageBitmap(resource.data)
            layoutBinding.apply {
                shimmerLayout.stopShimmer()
                shimmerLayout.visibility = View.GONE
            }
            layoutBinding.contentLayout.visibility = View.VISIBLE
        }
        if (resource.isError()) Log.v("TAG", "${resource.message}")
        dependency.taskManager.removeTask(id)
    }

    private fun clearLayout(wallpaperUI: WallpaperUI.Wallpaper) {
        layoutBinding.likes.text = formatLikesAndViews(wallpaperUI.wallpaper.likes)
        layoutBinding.views.text = formatLikesAndViews(wallpaperUI.wallpaper.views)
        layoutBinding.wallpaperCard.setOnClickListener {
            dependency.onClick(wallpaperUI)
        }
        layoutBinding.image.setImageBitmap(null)
        layoutBinding.contentLayout.visibility = View.GONE
        layoutBinding.shimmerLayout.apply {
            visibility = View.VISIBLE
            startShimmer()
        }
    }

    override fun bind(wallpaperUI: WallpaperUI) {
        if (wallpaperUI !is WallpaperUI.Wallpaper) return
        if(!showBottomLayout()) layoutBinding.bottomLayout.visibility = View.GONE
        clearLayout(wallpaperUI)
        val task = Task.createTask<WallpaperUI.Wallpaper, Resource<Bitmap>>(
            dependency.coroutineScope,
            wallpaperUI
        )
        task.addExecution { input -> dependency.viewModel.getImage(input) }
        task.addCallback { setLayout(it) }
        dependency.taskManager.addTask(id, task.build())
    }

    private fun formatLikesAndViews(value: Long): String {
        return if (value > 1000) String.format("%.1fk", value.toDouble().div(1000.0))
        else value.toString()
    }

    companion object {
        fun getLayoutBinding(parent: ViewGroup) : LayoutWallpaperItemBinding {
            return LayoutWallpaperItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        }
    }

    class ViewHolderDependency(
        val viewModel: WallpapersViewModel,
        val coroutineScope: CoroutineScope,
        val taskManager: TaskManager<WallpaperUI.Wallpaper, Resource<Bitmap>>,
        val onClick: (WallpaperUI.Wallpaper) -> Unit
    )
}