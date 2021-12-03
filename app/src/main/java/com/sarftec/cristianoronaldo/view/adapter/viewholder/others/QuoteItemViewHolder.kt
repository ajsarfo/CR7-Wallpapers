package com.sarftec.cristianoronaldo.view.adapter.viewholder.others

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sarftec.cristianoronaldo.databinding.LayoutWallpaperDetailBinding
import com.sarftec.cristianoronaldo.utils.Resource
import com.sarftec.cristianoronaldo.view.model.QuoteUI
import com.sarftec.cristianoronaldo.view.task.Task
import com.sarftec.cristianoronaldo.view.task.TaskManager
import com.sarftec.cristianoronaldo.view.viewmodel.QuoteViewModel
import kotlinx.coroutines.CoroutineScope
import java.util.*

class QuoteItemViewHolder private constructor(
    private val layoutBinding: LayoutWallpaperDetailBinding,
    private val dependency: ViewHolderDependency
) : RecyclerView.ViewHolder(layoutBinding.root){

    private val id = UUID.randomUUID().toString()

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
        dependency.taskManager.removeTask(id)
    }

    fun bind(position: Int, quoteUI: QuoteUI) {
        if (quoteUI !is QuoteUI.Quote) return
        dependency.viewModel.setAtPosition(position, quoteUI)
        clearLayout()
        val task = Task.createTask<QuoteUI.Quote, Resource<Bitmap>>(
            dependency.coroutineScope,
            quoteUI
        )
        task.addExecution { input -> dependency.viewModel.getImage(input) }
        task.addCallback { setLayout(it) }
        dependency.taskManager.addTask(id, task.build())
    }

    companion object {
        fun getInstance(
            parent: ViewGroup,
            dependency: ViewHolderDependency
        ): QuoteItemViewHolder {
            val binding = LayoutWallpaperDetailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return QuoteItemViewHolder(binding, dependency)
        }
    }

    class ViewHolderDependency(
        val coroutineScope: CoroutineScope,
        val viewModel: QuoteViewModel,
        val taskManager: TaskManager<QuoteUI.Quote, Resource<Bitmap>>
    )
}