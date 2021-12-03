package com.sarftec.cristianoronaldo.data.firebase.mapper

import com.sarftec.cristianoronaldo.data.firebase.model.FirebaseWallpaper
import com.sarftec.cristianoronaldo.domain.model.CR7Wallpaper
import javax.inject.Inject

class FirebaseWallpaperMapper @Inject constructor() {

    fun toFirebaseWallpaper(wallpaper: CR7Wallpaper) : FirebaseWallpaper {
        return FirebaseWallpaper(
            id = wallpaper.id,
            likes = wallpaper.likes,
            image = wallpaper.imageLocation,
            views = wallpaper.views,
            category = wallpaper.category,
            section = wallpaper.section
        )
    }

    fun toCR7Wallpaper(wallpaper: FirebaseWallpaper) : CR7Wallpaper {
        return CR7Wallpaper(
            id = wallpaper.id!!,
            likes = wallpaper.likes ?: 1500,
            imageLocation = wallpaper.image!!,
            views = wallpaper.views ?: 400,
            section = wallpaper.section!!,
            category = wallpaper.category!!
        )
    }
}