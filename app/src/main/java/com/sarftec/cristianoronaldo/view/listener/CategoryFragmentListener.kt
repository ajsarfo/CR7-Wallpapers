package com.sarftec.cristianoronaldo.view.listener

import com.sarftec.cristianoronaldo.view.model.CategoryUI

interface CategoryFragmentListener {
    fun navigateToDetailCategory(categoryUI: CategoryUI.Category)
}