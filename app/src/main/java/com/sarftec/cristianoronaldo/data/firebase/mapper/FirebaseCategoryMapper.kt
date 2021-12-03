package com.sarftec.cristianoronaldo.data.firebase.mapper

import com.sarftec.cristianoronaldo.data.firebase.model.FirebaseCategory
import com.sarftec.cristianoronaldo.domain.model.CR7Category
import javax.inject.Inject

class FirebaseCategoryMapper @Inject constructor() {

    fun toFirebaseCategory(category: CR7Category) : FirebaseCategory {
        return FirebaseCategory(
            category.id,
            category.name,
            category.total,
            category.imageLocation,
        )
    }

    fun toCR7Category(category: FirebaseCategory) : CR7Category {
        return CR7Category(
            category.id!!,
            category.name!!,
            category.total!!,
            category.image!!
        )
    }
}