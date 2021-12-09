package com.sarftec.cristianoronaldo.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sarftec.cristianoronaldo.data.room.converter.UriConverter
import com.sarftec.cristianoronaldo.data.room.dao.RoomFavoriteDao
import com.sarftec.cristianoronaldo.data.room.dao.RoomImageDao
import com.sarftec.cristianoronaldo.data.room.entity.RoomFavorite
import com.sarftec.cristianoronaldo.data.room.entity.RoomImage

@Database(
    entities = [
        RoomImage::class,
        RoomFavorite::class
    ],
    version = 1, exportSchema = false
)
@TypeConverters(UriConverter::class)
abstract class CR7Database : RoomDatabase() {

    abstract fun roomImageDao(): RoomImageDao
    abstract fun roomFavoriteWallpaperDao(): RoomFavoriteDao

    companion object {
        fun getInstance(context: Context): CR7Database {
            return Room.databaseBuilder(
                context,
                CR7Database::class.java,
                "cr7_wallpaper.db"
            ).build()
        }
    }
}