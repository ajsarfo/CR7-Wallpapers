package com.sarftec.cristianoronaldo.view.mapper

import com.sarftec.cristianoronaldo.domain.model.CR7Category
import com.sarftec.cristianoronaldo.view.model.CategoryUI
import javax.inject.Inject

class CategoryUIMapper @Inject constructor() {

    fun toCategoryUI(category: CR7Category) : CategoryUI {
        return CategoryUI.Category(category)
    }

    fun toCR7Category(categoryUI: CategoryUI) : CR7Category? {
        if(categoryUI !is CategoryUI.Category) return null
        return categoryUI.category
    }
}