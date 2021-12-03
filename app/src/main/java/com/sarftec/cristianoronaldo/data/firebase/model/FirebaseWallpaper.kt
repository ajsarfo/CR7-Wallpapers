package com.sarftec.cristianoronaldo.data.firebase.model

import com.google.firebase.firestore.Exclude

class FirebaseWallpaper(
    @Exclude
    var id: Long? = null,
    var likes: Long? = null,
    var views: Long? = null,
    var image: String? = null,
    var section: String? = null,
    var category: String? = null,
    var isApproved: Boolean? = false
) {
    companion object {
        const val FIELD_ID = "id"
        const val FIELD_SECTION = "section"
        const val FIELD_CATEGORY = "category"
        const val FIELD_IS_APPROVED = "isApproved"
    }
}