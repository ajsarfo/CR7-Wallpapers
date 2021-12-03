package com.sarftec.cristianoronaldo.view.adapter.viewholder.others

import android.graphics.Bitmap
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sarftec.cristianoronaldo.databinding.LayoutWallpaperDetailBinding
import com.sarftec.cristianoronaldo.utils.Resource
import com.sarftec.cristianoronaldo.view.model.WallpaperUI
import com.sarftec.cristianoronaldo.view.task.Task
import com.sarftec.cristianoronaldo.view.task.TaskManager
import com.sarftec.cristianoronaldo.view.viewmodel.DetailBaseViewModel
import kotlinx.coroutines.CoroutineScope
import java.util.*

class WallpaperDetailViewHolder<T : Parcelable> private constructor(
    private val layoutBinding: LayoutWallpaperDetailBinding,
    private val dependency: ViewHolderDependency<T>
) : RecyclerView.ViewHolder(layoutBinding.root) {

    private val id = UUID.randomUUID().toString()

    private val taskManager = TaskManager<WallpaperUI.Wallpaper, Resource<Bitmap>>()

    private fun clearLayout() {
        layoutBinding.image.visibility = View.GONE
        layoutBinding.loadingLayout.visibility = View.VISIBLE
        layoutBinding.loadingSpinner.playAnimation()
    }

    private fun setLayout(resource: Resource<Bitmap>) {
        layoutBinding.image.visibility = View.VISIBLE
        layoutBinding.loadingLayout.visibility = View.GONE
        layoutBinding.loadingSpinner.pauseAnimation()
        if (resource.isSuccess()) layoutBinding.image.setImageBitmap(resource.data!!)
        if (resource.isError()) Log.v("TAG", "${resource.message}")
        taskManager.removeTask(id)
    }

    fun bind(position: Int, wallpaperUI: WallpaperUI) {
        if (wallpaperUI !is WallpaperUI.Wallpaper) return
        Log.v("TAG", "Binding position => $position")
        dependency.viewModel.setAtPosition(position, wallpaperUI)
        clearLayout()
        val task = Task.createTask<WallpaperUI.Wallpaper, Resource<Bitmap>>(
            dependency.coroutineScope,
            wallpaperUI
        )
        task.addExecution { input -> dependency.viewModel.getImage(input) }
        task.addCallback { setLayout(it) }
        taskManager.addTask(id, task.build())
    }

    companion object {

        fun <T : Parcelable> getInstance(
            parent: ViewGroup,
            dependency: ViewHolderDependency<T>
        ): WallpaperDetailViewHolder<T> {
            val binding = LayoutWallpaperDetailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return WallpaperDetailViewHolder(binding, dependency)
        }

    }

    class ViewHolderDependency<T : Parcelable>(
        val viewModel: DetailBaseViewModel<T>,
        val coroutineScope: CoroutineScope
    )
}