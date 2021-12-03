package com.sarftec.cristianoronaldo.view.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.sarftec.cristianoronaldo.view.model.CategoryUI

object CategoryItemDiffUtil : DiffUtil.ItemCallback<CategoryUI>() {

    override fun areItemsTheSame(oldItem: CategoryUI, newItem: CategoryUI): Boolean {
        return if (oldItem is CategoryUI.Category && newItem is CategoryUI.Category) {
            oldItem.category.name == newItem.category.name
        } else (oldItem.id == newItem.id)
    }

    override fun areContentsTheSame(oldItem: CategoryUI, newItem: CategoryUI): Boolean {
        return if (oldItem is CategoryUI.Category && newItem is CategoryUI.Category) {
            oldItem.category == newItem.category
        } else (oldItem.id == newItem.id)
    }
}