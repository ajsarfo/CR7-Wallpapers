package com.sarftec.cristianoronaldo.data.room.mapper

import com.sarftec.cristianoronaldo.data.room.entity.RoomFavorite
import com.sarftec.cristianoronaldo.domain.model.CR7Wallpaper
import javax.inject.Inject

class RoomFavoriteMapper @Inject constructor(){

    fun toRoomFavoriteWallpaper (wallpaper: CR7Wallpaper) : RoomFavorite {
        return RoomFavorite(
            wallpaper.id.toString(),
            wallpaper.imageLocation
        )
    }

    fun toCR7Wallpaper(wallpaper: RoomFavorite) : CR7Wallpaper {
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