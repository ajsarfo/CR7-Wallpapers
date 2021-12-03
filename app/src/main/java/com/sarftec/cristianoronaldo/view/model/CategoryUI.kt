package com.sarftec.cristianoronaldo.view.model

import com.sarftec.cristianoronaldo.domain.model.CR7Category

sealed class CategoryUI(val id: Int) {
    class Category(val category: CR7Category): CategoryUI(UI_MODEL)
    companion object {
        const val UI_MODEL = 0
    }
}