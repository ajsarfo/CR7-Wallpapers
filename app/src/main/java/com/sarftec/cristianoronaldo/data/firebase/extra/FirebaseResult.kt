package com.sarftec.cristianoronaldo.data.firebase.extra

import com.sarftec.cristianoronaldo.domain.model.CR7Wallpaper

class FirebaseResult(
    val data: List<CR7Wallpaper>,
    var nextKey: FirebaseKey? = null,
    var previousKey: FirebaseKey? = null
)