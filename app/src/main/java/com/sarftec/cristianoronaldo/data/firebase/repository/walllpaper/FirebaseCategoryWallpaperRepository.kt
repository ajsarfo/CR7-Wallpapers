package com.sarftec.cristianoronaldo.data.firebase.repository.walllpaper

import com.google.firebase.firestore.Query
import com.sarftec.cristianoronaldo.data.firebase.model.FirebaseWallpaper

class FirebaseCategoryWallpaperRepository(
    private val category: String
) : FirebaseBaseRepository() {

    override fun getQuery(): Query {
        return collectionRef.whereEqualTo(FirebaseWallpaper.FIELD_CATEGORY, category)
    }
}