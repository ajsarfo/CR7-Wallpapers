package com.sarftec.cristianoronaldo.view.adapter.viewholder.others

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.sarftec.cristianoronaldo.databinding.LayoutWallpaperCategoryBinding
import com.sarftec.cristianoronaldo.utils.Resource
import com.sarftec.cristianoronaldo.view.model.CategoryUI
import com.sarftec.cristianoronaldo.view.task.Task
import com.sarftec.cristianoronaldo.view.task.TaskManager
import com.sarftec.cristianoronaldo.view.viewmodel.CategoryViewModel
import kotlinx.coroutines.CoroutineScope
import java.util.*

class CategoryItemViewHolder(
private val layoutBinding: LayoutWallpaperCategoryBinding,
private val dependency: ViewHolderDependency
) : RecyclerView.ViewHolder(layoutBinding.root) {

    private val id = UUID.randomUUID().toString()

    private fun setLayout(resource: Resource<Uri>) {
        layoutBinding.contentLayout.visibility  = View.VISIBLE
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
                               layoutBinding.contentLayout.visibility  = View.VISIBLE
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

    private fun clearLayout(categoryUI: CategoryUI.Category) {
        layoutBinding.apply {
            total.text = "${categoryUI.category.total} Images"
            title.text = "${categoryUI.category.name}"
        }
        layoutBinding.categoryCard.setOnClickListener {
            dependency.onClick(categoryUI)
        }
        layoutBinding.image.setImageBitmap(null)
        layoutBinding.contentLayout.visibility = View.GONE
        layoutBinding.shimmerLayout.apply {
            visibility = View.VISIBLE
            startShimmer()
        }
    }

    fun bind(categoryUI: CategoryUI) {
        if(categoryUI !is CategoryUI.Category) return
        clearLayout(categoryUI)
        val task = Task.createTask<CategoryUI.Category, Resource<Uri>>(
            dependency.coroutineScope,
            categoryUI
        )
        task.addExecution { input -> dependency.viewModel.getImage(input) }
        task.addCallback { setLayout(it) }
        dependency.taskManager.addTask(id, task.build())
    }

    companion object {
        fun getInstance(
            parent: ViewGroup,
            dependency: ViewHolderDependency,
        ) : CategoryItemViewHolder {
            val binding = LayoutWallpaperCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return CategoryItemViewHolder(binding, dependency)
        }
    }

    class ViewHolderDependency(
        val coroutineScope: CoroutineScope,
        val viewModel: CategoryViewModel,
        val taskManager: TaskManager<CategoryUI.Category, Resource<Uri>>,
        val onClick: (CategoryUI.Category) -> Unit
    )
}