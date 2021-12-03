package com.sarftec.cristianoronaldo.data.firebase.model

import com.google.firebase.firestore.Exclude

class FirebaseCategory(
    @Exclude
    var id: String? = null,
    var name: String? = null,
    val total: Long? = null,
    val image: String? = null
) {
    companion object {
        const val TOTAL_FIELD = "total"
    }
}