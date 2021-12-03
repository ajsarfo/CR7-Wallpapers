package com.sarftec.cristianoronaldo.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sarftec.cristianoronaldo.data.room.dao.RoomFavoriteWallpaperDao
import com.sarftec.cristianoronaldo.data.room.dao.RoomImageDao
import com.sarftec.cristianoronaldo.data.room.entity.RoomFavoriteWallpaper
import com.sarftec.cristianoronaldo.data.room.entity.RoomImage

@Database(
    entities = [
        RoomImage::class,
        RoomFavoriteWallpaper::class
    ],
    version = 1, exportSchema = false
)
@TypeConverters(BitmapTypeConverter::class)
abstract class CR7RoomDatabase : RoomDatabase() {

    abstract fun roomImageDao(): RoomImageDao
    abstract fun roomFavoriteWallpaperDao(): RoomFavoriteWallpaperDao

    companion object {
        fun getInstance(context: Context): CR7RoomDatabase {
            return Room.databaseBuilder(
                context,
                CR7RoomDatabase::class.java,
                "cr7_wallpaper.db"
            ).build()
        }
    }
}