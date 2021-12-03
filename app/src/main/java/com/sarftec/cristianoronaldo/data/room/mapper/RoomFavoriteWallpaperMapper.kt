package com.sarftec.cristianoronaldo.data.room.mapper

import com.sarftec.cristianoronaldo.data.room.entity.RoomFavoriteWallpaper
import com.sarftec.cristianoronaldo.domain.model.CR7Wallpaper
import javax.inject.Inject

class RoomFavoriteWallpaperMapper @Inject constructor(){

    fun toRoomFavoriteWallpaper (wallpaper: CR7Wallpaper) : RoomFavoriteWallpaper {
        return RoomFavoriteWallpaper(
            wallpaper.id.toString(),
            wallpaper.imageLocation
        )
    }

    fun toCR7Wallpaper(wallpaper: RoomFavoriteWallpaper) : CR7Wallpaper {
        return CR7Wallpaper(
            wallpaper.id.toLong(),
            0,
            0,
            wallpaper.imageLocation,
            "favorite",
            "",
            true
        )
    }
}