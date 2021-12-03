package com.sarftec.cristianoronaldo.data.firebase.repository.walllpaper

import com.google.firebase.firestore.Query
import com.sarftec.cristianoronaldo.data.firebase.model.FirebaseWallpaper
import com.sarftec.cristianoronaldo.utils.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseApproveWallpaperRepository @Inject constructor(

) : FirebaseBaseRepository() {

    override fun getQuery(): Query {
        return collectionRef.whereEqualTo(FirebaseWallpaper.FIELD_IS_APPROVED, false)
    }

    suspend fun approveWallpaper(wallpaper: FirebaseWallpaper): Resource<Unit> {
        return try {
            collectionRef.document(wallpaper.id.toString())
                .update(FirebaseWallpaper.FIELD_IS_APPROVED, true)
                .await()
            Resource.success(Unit)
        } catch (e: Exception) {
            Resource.error(e.message)
        }
    }
}