package com.sarftec.cristianoronaldo.data.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sarftec.cristianoronaldo.data.ROOM_FAVORITE_TABLE
import com.sarftec.cristianoronaldo.data.room.entity.RoomFavorite

@Dao
interface RoomFavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallpaper(wallpaper: RoomFavorite)

    @Query("select * from $ROOM_FAVORITE_TABLE")
    fun getPagingSource() : PagingSource<Int, RoomFavorite>

    @Query("select * from $ROOM_FAVORITE_TABLE")
    suspend fun getWallpapers() : List<RoomFavorite>

    @Query("select * from $ROOM_FAVORITE_TABLE where id = :id")
    suspend fun getWallpaper(id: String) : RoomFavorite?

     @Query("delete from $ROOM_FAVORITE_TABLE")
     suspend fun removeWallpapers()

     @Query("delete from $ROOM_FAVORITE_TABLE where id = :id")
     suspend fun removeWallpaper(id: String)
}