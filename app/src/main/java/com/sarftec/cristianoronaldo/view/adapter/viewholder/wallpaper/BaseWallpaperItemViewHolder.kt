package com.sarftec.cristianoronaldo.view.adapter.viewholder.wallpaper

import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
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

    private fun setLayout(resource: Resource<Uri>) {
        layoutBinding.contentLayout.visibility = View.VISIBLE
        if (resource.isSuccess()) {
            if (resource.isSuccess()) {
                Glide.with(itemView)
                    .load(resource.data!!)
                    .addListener(
                        object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                Log.v("TAG", "Error => Glide load failed!")
                                dependency.taskManager.removeTask(id)
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                Log.v("TAG", "Success => Glide load completed!")
                                dependency.taskManager.removeTask(id)
                                layoutBinding.apply {
                                    shimmerLayout.stopShimmer()
                                    shimmerLayout.visibility = View.GONE
                                }
                                layoutBinding.contentLayout.visibility = View.VISIBLE
                                return false
                            }
                        }
                    )
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(layoutBinding.image)
            }
        }
        if (resource.isError()) {
            dependency.taskManager.removeTask(id)
            Log.v("TAG", "${resource.message}")
        }
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
        val task = Task.createTask<WallpaperUI.Wallpaper, Resource<Uri>>(
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
        val taskManager: TaskManager<WallpaperUI.Wallpaper, Resource<Uri>>,
        val onClick: (WallpaperUI.Wallpaper) -> Unit
    )
}