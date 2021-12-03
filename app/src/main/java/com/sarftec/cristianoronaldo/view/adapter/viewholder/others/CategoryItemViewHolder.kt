package com.sarftec.cristianoronaldo.view.adapter.viewholder.others

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
        val task = Task.createTask<CategoryUI.Category, Resource<Bitmap>>(
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
        val taskManager: TaskManager<CategoryUI.Category, Resource<Bitmap>>,
        val onClick: (CategoryUI.Category) -> Unit
    )
}