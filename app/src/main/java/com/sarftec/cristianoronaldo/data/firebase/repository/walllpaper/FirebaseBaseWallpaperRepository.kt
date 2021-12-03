package com.sarftec.cristianoronaldo.data.firebase.repository.walllpaper

import com.google.firebase.firestore.Query
import com.sarftec.cristianoronaldo.data.firebase.model.FirebaseWallpaper

abstract class FirebaseBaseWallpaperRepository(
    private val section: Section
) : FirebaseBaseRepository() {

    override fun getQuery(): Query {
        return collectionRef.whereEqualTo(FirebaseWallpaper.FIELD_SECTION, section.id)
            //.whereEqualTo(FirebaseWallpaper.FIELD_IS_APPROVED, true)
    }

    enum class Section(val id: String) {
        RECENT("recent"), POPULAR("popular")
    }
}