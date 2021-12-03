package com.sarftec.cristianoronaldo.view.adapter

import android.graphics.Bitmap
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sarftec.cristianoronaldo.utils.Resource
import com.sarftec.cristianoronaldo.view.adapter.viewholder.others.CategoryItemViewHolder
import com.sarftec.cristianoronaldo.view.model.CategoryUI
import com.sarftec.cristianoronaldo.view.task.TaskManager
import com.sarftec.cristianoronaldo.view.viewmodel.CategoryViewModel
import kotlinx.coroutines.CoroutineScope

class CategoryAdapter(
    coroutineScope: CoroutineScope,
    viewModel: CategoryViewModel,
    onClick: (CategoryUI.Category) -> Unit
) : RecyclerView.Adapter<CategoryItemViewHolder>() {

    private var items: List<CategoryUI> = listOf()

    private val taskManager = TaskManager<CategoryUI.Category, Resource<Bitmap>>()

    private val dependency = CategoryItemViewHolder.ViewHolderDependency(
        coroutineScope,
        viewModel,
        taskManager,
        onClick
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemViewHolder {
        return CategoryItemViewHolder.getInstance(parent, dependency)
    }

    override fun onBindViewHolder(holder: CategoryItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun submitData(categoryUIs: List<CategoryUI>) {
        items = categoryUIs
        notifyDataSetChanged()
    }
}