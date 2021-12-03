package com.sarftec.cristianoronaldo.view.adapter

import android.graphics.Bitmap
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sarftec.cristianoronaldo.utils.Resource
import com.sarftec.cristianoronaldo.view.adapter.viewholder.others.QuoteItemViewHolder
import com.sarftec.cristianoronaldo.view.model.QuoteUI
import com.sarftec.cristianoronaldo.view.task.TaskManager
import com.sarftec.cristianoronaldo.view.viewmodel.QuoteViewModel
import kotlinx.coroutines.CoroutineScope

class QuoteAdapter(
    coroutineScope: CoroutineScope,
    viewModel: QuoteViewModel
) : RecyclerView.Adapter<QuoteItemViewHolder>() {

    private var items = listOf<QuoteUI>()

    private val taskManager = TaskManager<QuoteUI.Quote, Resource<Bitmap>>()

    private val dependency = QuoteItemViewHolder.ViewHolderDependency(
        coroutineScope,
        viewModel,
        taskManager
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteItemViewHolder {
        return QuoteItemViewHolder.getInstance(parent, dependency)
    }

    override fun onBindViewHolder(holder: QuoteItemViewHolder, position: Int) {
        holder.bind(position, items[position])
    }

    override fun getItemCount(): Int = items.size

    fun submitData(items: List<QuoteUI>) {
        this.items = items
        notifyDataSetChanged()
    }
}