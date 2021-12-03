package com.sarftec.cristianoronaldo.data.firebase.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.sarftec.cristianoronaldo.data.firebase.model.FirebaseCategory
import com.sarftec.cristianoronaldo.data.firebase.model.FirebaseWallpaper
import com.sarftec.cristianoronaldo.utils.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseCategoryRepository @Inject constructor(

) {

    private val storeRef = FirebaseFirestore.getInstance()
    private val collectionRef = storeRef.collection("categories")

    suspend fun addWallpaperToCategory(wallpaper: FirebaseWallpaper) : Resource<Unit> {
        return try {
            storeRef.runTransaction { transaction ->
                val documentRef = collectionRef.document(wallpaper.category!!)
                val snapshot = transaction.get(documentRef)
                val newCount = snapshot
                    .getLong(FirebaseCategory.TOTAL_FIELD)
                    ?.plus(1)
                transaction.update(
                    documentRef,
                    FirebaseCategory.TOTAL_FIELD,
                    newCount
                )
            }
           Resource.success(Unit)
        } catch (e: Exception) {
            Resource.error(e.message)
        }
    }

    suspend fun getCategories() : Resource<List<FirebaseCategory>> {
        return try {
            val categories = collectionRef.get().await().map {
                it.toObject(FirebaseCategory::class.java).also { category ->
                    category.id = it.id
                }
            }
            Resource.success(categories)
        } catch (e: Exception) {
            Resource.error(e.message)
        }
    }
}